# Configuration

Default settings are read from `core/src/main/resources/game.conf`.
The file controls the network buffer size, autosave interval and server host/ports.
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

The `game.server.host` key defines the hostname or IP address that clients
should use when connecting to the server.

Saves and user preferences are written to a platform specific directory. The
exact locations are resolved by
[`Paths`](../core/src/main/java/net/lapidist/colony/io/Paths.java).

## Save Format

Save version 8 introduces a new `playerPos` field in `MapState` storing the player's tile
coordinates. Older saves are migrated automatically with the position initialized to the
center of the world.
