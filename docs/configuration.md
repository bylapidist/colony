# Configuration

Default settings are read from `core/src/main/resources/game.conf`.
The file controls the network buffer size, autosave interval, server host and ports.
Map size is determined when creating a game or loading a save:

```hocon
game {
  tileSize = 32
  autosaveInterval = 100000
  defaultSaveName = "autosave"
    networkBufferSize = 8388608
  server {
    host = "localhost"
    tcpPort = 54555
    udpPort = 54777
  }
}
```

Saves and user preferences are written to a platform specific directory. The
exact locations are resolved by
[`Paths`](../core/src/main/java/net/lapidist/colony/io/Paths.java).

## Save Format

Save version 8 introduces a new `playerPos` field in `MapState` storing the player's tile
coordinates. Older saves are migrated automatically with the position initialized to the
center of the world.
Save versions are tracked by the `SaveVersion` enum. Each constant stores its numeric representation and `CURRENT` points to the latest version:

```java
public enum SaveVersion {
    V1(1),
    V2(2),
    // ...
    V28(28),
    V29(29);

    public static final SaveVersion CURRENT = V29;
}
```

When a game is loaded, `SaveMigrator` applies registered `MapStateMigration` steps to upgrade old data. Each migration class converts from one version to the next and is registered once in the static block of `SaveMigrator`.

### Adding a New Version

1. Append the new constant to `SaveVersion` and update `CURRENT`.
2. Implement a migration class:

```java
public final class V19ToV20Migration implements MapStateMigration {
    @Override public int fromVersion() { return SaveVersion.V19.number(); }
    @Override public int toVersion() { return SaveVersion.V20.number(); }
    @Override public MapState apply(MapState state) {
        // update fields here
        return state.toBuilder().version(toVersion()).build();
    }
}
```

3. Register the new migration inside `SaveMigrator`.

Whenever a serialized class list changes, recompute `SerializationRegistrar.REGISTRATION_HASH` using `registrationHash()` so mismatched saves are detected.
## Graphics Settings

Rendering options are grouped under the `graphics` prefix inside `settings.conf`.
The most important key is `shaderPlugin` which selects the shader implementation
used by the map renderer. By default it is set to `lights-normalmap`, a plugin
that combines dynamic lighting from Box2D-lights with normal map rendering:

```properties
graphics.shaderPlugin=lights-normalmap
```

Lighting can be disabled independently with `graphics.lighting=false` if the
additional frame buffer passes are too costly on low end GPUs.

The number of rays per dynamic light can be tuned with `graphics.lightRays`.
Higher values produce smoother shadows but reduce performance. The default is
`16`.

