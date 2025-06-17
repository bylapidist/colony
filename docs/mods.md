# Modding Overview

This guide covers how to organize your `mods/` folder and how `ModLoader` discovers mod implementations.

When developing new gameplay for Colony itself, try to implement the feature as
a built-in core mod under `server/src/main/java/net/lapidist/colony/base` before
expanding the main modules. Core mods keep functionality modular and optional.

## Folder layout

All mods live under the `mods/` directory inside the game folder.
Each entry can be either a JAR file or a directory with the following structure:

```
mods/
  example.jar
  othermod/
    mod.json
    META-INF/
      services/
        net.lapidist.colony.mod.GameMod
```

Every directory must contain a `mod.json` file and the standard
`META-INF/services` descriptor listing the implementing class of `GameMod`.
JAR files embed these files within the archive.

A minimal `mod.json` looks like:

```json
{
  "id": "example",
  "version": "1.0.0",
  "dependencies": []
}
```

`id` is the unique identifier used for dependency resolution.
`version` is an arbitrary string.
`dependencies` lists other mod IDs that must be loaded first.

### Built-in server mods

The dedicated server bundles several mods providing the default functionality.
These use the same version as the running server and are loaded automatically, so they do not need to be placed inside the `mods/` folder:

```json
{ "id": "base-map-service" }
{ "id": "base-network" }
{ "id": "base-autosave" }
{ "id": "base-resource-production" }
{ "id": "base-handlers" }
{ "id": "base-definitions" }
{ "id": "base-resources" }
{ "id": "base-items" }
```

## How mods are discovered

`ModLoader` scans the `mods/` folder and creates a `URLClassLoader` for every
JAR or directory it finds. Each class loader is passed to Java's `ServiceLoader`
to locate implementations of the `GameMod` interface. If a `mod.json` is
missing, the entry is skipped.

### Dependency resolution

Dependencies declared in `mod.json` are used to determine the load order. The
loader performs a topological sort and removes entries with missing
dependencies or cycles, logging a warning for each skipped mod.

### Service and handler registration

`GameMod` exposes two hooks that run during server startup:

```java
default void registerServices(GameServer server) { }
default void registerHandlers(CommandBus bus) { }
```

`registerServices` allows mods to override `GameServer` factories or install
additional services before the server begins accepting connections. Once the
core services have been recreated, `registerHandlers` runs to register custom
command handlers. A minimal example:

```java
public final class ExtraMod implements GameMod {
    @Override
    public void registerServices(GameServer server) {
        server.setMapServiceFactory(() -> new MyMapService());
    }

    @Override
    public void registerHandlers(CommandBus bus) {
        bus.registerHandlers(List.of(new MyCommandHandler()));
    }
}
```

`GameServer` also exposes convenience methods for lifecycle management and
service customization. Mods may call `start`, `stop` or `broadcast` on the
provided instance. Core services can be replaced via the corresponding factory
setters: `setMapServiceFactory`, `setNetworkServiceFactory`,
`setAutosaveServiceFactory`, `setResourceProductionServiceFactory` and
`setCommandBusFactory`.

## Registries

Core game data is looked up from string keyed registries. Four registries are
provided via the `Registries` helper class:

- `tiles()` – `TileDefinition` entries
- `buildings()` – `BuildingDefinition` entries
- `resources()` – `ResourceDefinition` entries
- `items()` – `ItemDefinition` entries

Mods can register new entries during the `init()` phase:

```java
public final class WaterTiles implements GameMod {
    @Override
    public void init() {
        Registries.tiles().register(new TileDefinition("WATER", "Water", "water0"));
        Registries.items().register(new ItemDefinition("bucket", "Bucket", "bucket0"));
    }
}
```

After registration the new ID can be referenced by map generators or scripts.

## System hooks

`GameMod` exposes several lifecycle hooks used during server startup:

```
void init()
void registerServices(GameServer server)
void registerSystems(GameServer server)
void registerHandlers(CommandBus bus)
void dispose()
```

`registerSystems` lets mods schedule background tasks by providing implementations of
`GameSystem`. The server automatically starts registered systems once initialisation completes and
stops them when shutting down.

The `init()` and `dispose()` methods run when a mod is loaded and unloaded. Use them to register
definitions and release resources. Service and handler registration occurs between these stages.

## Scripting

Mods may ship Kotlin scripts inside a `scripts/` folder. When a mod is loaded the loader executes every
`*.kts` file found and the script may register event listeners using the `on` DSL function. The following
script prints a message whenever a tile is selected:

```kotlin
// scripts/tileSelect.kts
import net.lapidist.colony.server.events.TileSelectionEvent

on<TileSelectionEvent> { event ->
    if (event.selected()) {
        println("Selected tile at ${event.x()}, ${event.y()}")
    }
}
```

### Example: custom tile type

Combining registries and scripting allows mods to implement new gameplay elements. The following mod
registers a `WATER` tile type and prints a message whenever the player selects one:

```java
public final class WaterMod implements GameMod {
    @Override
    public void init() {
        Registries.tiles().register(new TileDefinition("WATER", "Water", "water0"));
    }
}
```

Place the `tileSelect.kts` script from the previous section in the mod's `scripts/` directory to respond to
`TileSelectionEvent`.


### Example: Hello World mod

A complete example is provided in the `examples/mods/hello-world` directory.
Compile the class and copy the folder into your game's `mods/` directory:

```bash
cd examples/mods/hello-world
javac -d . src/main/java/net/lapidist/colony/example/HelloWorldMod.java
cp -r . ../../../../mods/hello-world
```

Start the server with `./gradlew :server:run`. When the mod loads it prints
`Hello world from HelloWorldMod` to the console.

