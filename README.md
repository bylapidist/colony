# Colony
[![Java CI](https://github.com/bylapidist/colony/actions/workflows/gradle.yml/badge.svg)](https://github.com/bylapidist/colony/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/bylapidist/colony/graph/badge.svg?token=mCn1MJTldf)](https://codecov.io/gh/bylapidist/colony)

Colony is a small simulation/strategy prototype built with LibGDX and the Artemis-ODB entity component system. The project is structured as a multi-module Gradle build so the client, server and shared logic can evolve independently.

## Table of Contents
- [Quick Start](#quick-start)
  - [Building and Testing](#building-and-testing)
  - [Running the Game](#running-the-game)
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
Configuration details are in [docs/configuration.md](docs/configuration.md).

## License
This project is licensed under the [MIT License](LICENSE).

## Code of Conduct
Please read our [Code of Conduct](CODE_OF_CONDUCT.md) for guidelines on how to participate in this community.
