# Colony Game Design Document

## Overview
Colony is a lightweight tile-based simulation built with LibGDX, Artemis-ODB and
KryoNet. The current code base supports a client-server architecture, basic map
generation, tile selection and simple buildings. This document proposes a small
playable slice that can be implemented using the existing systems.

## Core Loop
1. The server generates or loads a map state containing tiles and buildings.
2. The client connects, receives the map and renders the world.
3. Players pan/zoom the camera and select tiles using the mouse or touch.
4. Selected tiles are highlighted locally and synced to the server.
5. Autosaves run periodically while the game is active.

## Map
- Grid size defined in `game.conf` (`mapWidth`, `mapHeight`, `tileSize`).
- `DefaultMapGenerator` creates a random layout of grass/dirt tiles and a single
  starting house.
- Tiles are entities with passability and selection state.

## Buildings
- `BuildingComponent` defines types: `HOUSE`, `MARKET`, `FACTORY`.
- `BuildingFactory` converts `BuildingData` into renderable entities.
- Initially only houses are placed by the generator, but additional types can be
  spawned through future systems.

## Controls
- WASD to scroll the map, mouse wheel or pinch to zoom.
- Clicking/tapping a tile toggles its selection and notifies the server.

## User Interface
- `MapScreen` renders the world and a simple UI with a minimap and menu button.
- `MinimapActor` shows the current camera view rectangle.

## Networking
- `GameServer` listens on ports specified in `game.conf` and sends the map to
  connecting clients.
- `GameClient` receives the map, sends tile selection updates and stops on
  request.

## Saving
- Autosaves are written at the interval specified in configuration using
  `GameStateIO` and stored under the user's platform-specific game folder.

## Future Work
- Building placement, resource management and AI characters can be layered on
  top of the existing ECS structure.

