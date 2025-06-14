# Configuration

Default settings are read from `core/src/main/resources/game.conf`. The file
controls the initial map size, network buffer size, autosave interval and server ports:

```hocon
game {
  mapWidth = 30
  mapHeight = 30
  tileSize = 32
  autosaveInterval = 600000
  defaultSaveName = "autosave"
    networkBufferSize = 8388608
  server {
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
