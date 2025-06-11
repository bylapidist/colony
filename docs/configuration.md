# Configuration

Default settings are read from `core/src/main/resources/game.conf`. The file
controls the initial map size, autosave interval and server ports:

```hocon
game {
  mapWidth = 30
  mapHeight = 30
  tileSize = 32
  autosaveInterval = 600000
  defaultSaveName = "autosave"
  server {
    tcpPort = 54555
    udpPort = 54777
  }
}
```

Saves and user preferences are written to a platform specific directory. The
exact locations are resolved by
[`Paths`](../core/src/net/lapidist/colony/io/Paths.java).
