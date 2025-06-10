# Colony
[![Java CI](https://github.com/bylapidist/colony/actions/workflows/gradle.yml/badge.svg)](https://github.com/bylapidist/colony/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/bylapidist/colony/graph/badge.svg?token=mCn1MJTldf)](https://codecov.io/gh/bylapidist/colony)

Colony is a small simulation/strategy prototype built with LibGDX and the Artemis-ODB entity component system. The project is structured as a multi-module Gradle build so the client, server and shared logic can evolve independently.

## Table of Contents
- [Quick Start](#quick-start)
  - [Building and Testing](#building-and-testing)
  - [Running the Game](#running-the-game)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Networking Workflow](#networking-workflow)
- [Development Guidelines](#development-guidelines)
  - [Code Style](#code-style)
  - [Contributing](#contributing)
  - [Documentation](#documentation)

## Quick Start
### Building and Testing
Java 21 toolchains are configured via the Gradle wrapper. Run the following commands whenever you pull new changes or before committing:

```bash
./gradlew tests:copyAssets   # copy client assets into the test module
./gradlew spotlessApply      # format the code base
./gradlew clean test         # build and execute all tests
./gradlew check              # run Checkstyle and Spotless verification
```

You can also run all of these steps at once:

```bash
./scripts/check.sh
```

The `tests:copyAssets` task is required so that resources used by the test suite are available. `spotlessApply` will automatically format all Java sources and must be executed before committing.

### Running the Game
Both the client and dedicated server can be started directly from Gradle:

```bash
./gradlew :client:run   # start the game client
./gradlew :server:run   # start the dedicated server
```

## Project Structure
The repository is split into four Gradle modules:

- **core** – shared ECS components, constants, cross-platform game logic and Kryo serializers.
- **client** – desktop client using LibGDX. This module contains the game loop, rendering logic, UI classes and networking code to communicate with the server.
- **server** – headless game server. It exposes networking services using Kryonet and runs the same ECS logic as the client to keep game state in sync.
- **tests** – JUnit tests and a custom `GdxTestRunner` that boots a headless LibGDX environment so game systems can be tested without a graphical context.

Each module keeps its source under `src/` with all packages rooted at `net.lapidist.colony`. Shared constants and configuration files live in the `core` module and are imported by both the client and server.

## Configuration
Configuration defaults such as map size, autosave interval and network ports are defined in `core/src/main/resources/game.conf` and loaded at runtime. Per-user saves and settings are written to a platform specific directory under the user's home folder. `core/src/net/lapidist/colony/io/Paths.java` resolves the exact locations.

All visible text is provided through resource bundles found in `core/src/main/resources/i18n`. The current locale can be changed in the in‑game settings screen and is persisted alongside the user's other settings.

## Networking Workflow
Multiplayer features follow a strict request/response pattern:

1. When a user action should modify the world, the client sends a **request** message describing the action to the server.
2. The server validates and applies the change, then **broadcasts** the resulting state update to all clients.
3. Clients **queue** incoming updates and apply them during their next update step.

Local changes must not be applied before the server's confirmation is processed. This keeps all connected clients in sync.

The code mirrors this flow with clearly named methods:

- Clients call `sendTileSelectionRequest` to issue actions.
- The server uses `broadcast` to relay updates to all clients.
- Each client processes queued updates via `poll(TileSelectionData.class)` inside its update systems.

For a step‑by‑step example see [docs/networking.md](docs/networking.md).

## Development Guidelines
### Code Style
Checkstyle rules are defined in `config/checkstyle/checkstyle.xml`. All Java files use four spaces for indentation and lines are limited to 120 characters. The Gradle `check` task enforces these conventions and should pass before committing.

### Contributing
New classes should be placed under the `net.lapidist.colony` package in the appropriate module. Commit messages and pull request titles follow the [Angular commit message guidelines](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#commit). When changing or adding functionality, ensure it has adequate test coverage. Scenario tests can be written with the `GameSimulation` utilities under the `tests` module.


For more details see [CONTRIBUTING.md](CONTRIBUTING.md).

## Documentation
The latest Java API reference is published to [GitHub Pages](https://bylapidist.github.io/colony/) on every release.
For a high level overview of the modules and networking flow see [docs/architecture.md](docs/architecture.md).
Refer to [docs/networking.md](docs/networking.md) for hands‑on client and server examples.

## License
This project is licensed under the [MIT License](LICENSE).

## Code of Conduct
Please read our [Code of Conduct](CODE_OF_CONDUCT.md) for guidelines on how to participate in this community.
