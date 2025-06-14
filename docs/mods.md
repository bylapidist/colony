# Modding Overview

This guide covers how to organize your `mods/` folder and how `ModLoader` discovers mod implementations.

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

### Built-in server mod

The dedicated server bundles a mod providing the default services and handlers.
It is loaded automatically with the following metadata:

```json
{ "id": "core-server", "version": "1.0.0" }
```

This mod does not need to be placed inside the `mods/` folder.

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

