# Colony
[![Java CI](https://github.com/bylapidist/colony/actions/workflows/gradle.yml/badge.svg)](https://github.com/bylapidist/colony/actions/workflows/gradle.yml)

Colony is a small simulation/strategy game prototype built with LibGDX and the Artemis-ODB
entity component system. The code base is organised as a multi-module Gradle project so that
engine components, game logic and tests can be developed independently.

## Version
The latest release is **v5.52.1**.

## Project structure
The repository is split into four Gradle modules:

- **components** – Shared code such as ECS components, configuration utilities and I/O helpers.
- **client** – Desktop client using LibGDX. This module contains the game loop, rendering logic,
  UI classes and networking code to communicate with the server.
- **server** – Headless game server. It exposes networking services using Kryonet and runs the
  same ECS logic as the client to keep game state in sync.
- **tests** – JUnit tests and a custom `GdxTestRunner` that boots a headless LibGDX environment
  so game systems can be tested without a graphical context.

Each module keeps its source under `src/` with all packages rooted at
`net.lapidist.colony`. Shared constants and configuration files live in the
`components` module and are imported by both the client and server.

## Building and testing
Java 21 toolchains are configured via the Gradle wrapper. To work on the project run the
following commands whenever you pull new changes or before committing:

```bash
./gradlew tests:copyAssets   # copy client assets into the test module
./gradlew spotlessApply      # format the code base
./gradlew clean test         # build and execute all tests
./gradlew check              # run Checkstyle and Spotless verification
```

The `tests:copyAssets` task is required so that resources used by the test suite are
available. `spotlessApply` will automatically format all Java sources and must be executed
before committing.

## Running the game
Both the client and dedicated server can be started directly from Gradle:

```bash
./gradlew :client:run   # start the game client
./gradlew :server:run   # start the dedicated server
```

Configuration files and save data are written to a platform specific directory under the
user's home folder. See `components/src/net/lapidist/colony/io/Paths.java` for the
exact locations.

## Coding style
Checkstyle rules are defined in `config/checkstyle/checkstyle.xml`. All Java files use
four spaces for indentation and lines are limited to 120 characters. The Gradle
`check` task enforces these conventions and should pass before committing.

## Contributing guidelines
New classes should be placed under the `net.lapidist.colony` package in the
appropriate module. Commit messages and pull request titles follow the
[Angular commit message guidelines](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#commit).
Ensure new functionality is covered by tests whenever possible.

