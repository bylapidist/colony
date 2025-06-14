# Colony
[![Java CI](https://github.com/bylapidist/colony/actions/workflows/gradle.yml/badge.svg)](https://github.com/bylapidist/colony/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/bylapidist/colony/graph/badge.svg?token=mCn1MJTldf)](https://codecov.io/gh/bylapidist/colony)

Colony is a small simulation/strategy prototype built with LibGDX and the Artemis-ODB entity component system. The project uses Kryonet for networking and includes a lightweight mod system. It is organized as a multi-module Gradle build so the client, server and shared logic can evolve independently.

## Table of Contents
- [Quick Start](#quick-start)
  - [Building and Testing](#building-and-testing)
  - [Running the Game](#running-the-game)
- [Key Entry Points](#key-entry-points)
- [Controls](#controls)
- [Architecture](#architecture)
- [Configuration](#configuration)
- [Networking](#networking)
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
./gradlew codeCoverageReport # generate the JaCoCo coverage report
```

You can also run all of these steps at once:

```bash
./scripts/check.sh
```

The `tests:copyAssets` task is required so that resources used by the test suite are available. `spotlessApply` will automatically format all Java sources and must be executed before committing. After running the tasks above, open `build/reports/jacoco/test/html/index.html` to review coverage results. New code should maintain at least 80% line coverage.
For more details on the developer workflow, see [docs/developer_workflow.md](docs/developer_workflow.md).

### Running the Game
Both the client and dedicated server can be started directly from Gradle:

- `./gradlew :client:run` – launches the game client.
- `./gradlew :server:run` – starts a headless server.

### Docker
A `Dockerfile` is available to run the server in a container. Build the image and expose the default ports:

```bash
docker build -t colony-server .
docker run -p 54555:54555/tcp -p 54777:54777/udp \
           -v $(pwd)/colony-data:/root/.colony colony-server
```

Game data is stored in the mounted `colony-data` directory. An example compose file is included for convenience:

```bash
docker compose up
```


## Key Entry Points
Useful starting classes for navigating the source tree:

- `client/src/main/java/net/lapidist/colony/client/network/GameClient.java` – networked client logic
- `server/src/main/java/net/lapidist/colony/server/GameServer.java` – main server entry and message handlers
- Core mods in `server/src/main/java/net/lapidist/colony/base` – built‑in gameplay modules
- `core/src/main/java/net/lapidist/colony/map/MapFactory.java` – map generation utilities
- `client/src/main/java/net/lapidist/colony/client/renderers/MapRendererFactory.java` – renderer abstraction
- `client/src/main/java/net/lapidist/colony/client/renderers/SpriteMapRendererFactory.java` – default renderer implementation
- `client/src/main/java/net/lapidist/colony/client/entities/BuildingFactory.java` – creates building entities
- `client/src/main/java/net/lapidist/colony/client/entities/TileFactory.java` – creates tile entities

## Controls
Default keyboard mappings can be remapped in game. See
[docs/controls.md](docs/controls.md) for the full list and instructions.

## Architecture
An overview of the project layout and renderer abstraction is available in
[docs/architecture.md](docs/architecture.md).

## Configuration
Game defaults and save locations are detailed in
[docs/configuration.md](docs/configuration.md).

## Networking
The client and server communicate using a request/response protocol. See
[docs/networking.md](docs/networking.md) for a detailed walkthrough and code
examples.

## Contributing
Please read [CONTRIBUTING.md](CONTRIBUTING.md) for coding conventions and the
development workflow.

## Documentation
The latest Java API reference is published to [GitHub Pages](https://bylapidist.github.io/colony/) on every release.
For a high level overview of the modules see [docs/architecture.md](docs/architecture.md).
Refer to [docs/networking.md](docs/networking.md) for hands‑on client and server examples.
See [docs/mods.md](docs/mods.md) for details on the enhanced mod system.
Configuration details are in [docs/configuration.md](docs/configuration.md).
Performance benchmark numbers are tracked in [docs/performance.md](docs/performance.md).
Translation instructions are in [docs/i18n.md](docs/i18n.md).
Scenario test utilities are covered in [docs/tests.md](docs/tests.md).
Developer build steps are summarized in [docs/developer_workflow.md](docs/developer_workflow.md).
Shader plugins are described in [docs/shaders.md](docs/shaders.md).
The asset pipeline is documented in [docs/asset_pipeline.md](docs/asset_pipeline.md).

## License
This project is licensed under the [MIT License](LICENSE).

## Code of Conduct
Please read our [Code of Conduct](CODE_OF_CONDUCT.md) for guidelines on how to participate in this community.
