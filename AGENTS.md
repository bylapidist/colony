# Colony AGENT Guide

## Project Layout
This repository is a multi-module Java project built with Gradle. The modules are:

- **core** – shared game logic, ECS components and serializers
- **client** – desktop LibGDX client
- **server** – headless game server
- **tests** – JUnit test utilities and suites
- **docs** – developer and player guides

Each module contains its own `AGENTS.md` with extra notes.

## Getting Started
- Java 21 toolchains are provided via Gradle, no local JDK is required.
- Run `./gradlew tests:copyAssets` to copy client assets for the test module.
- Run `./gradlew spotlessApply` to format the code base.
- Run `./gradlew clean test` to compile and execute tests.
- Run `./gradlew check` to verify Checkstyle and Spotless rules.
- The helper script `./scripts/check.sh` performs all steps at once.

## Running the game
- `./gradlew :client:run` – start the desktop client.
- `./gradlew :server:run` – start the dedicated server.

## Development Conventions
- Four space indentation, no tab characters, line length under 120 characters.
- Place new classes under the `net.lapidist.colony` package in the appropriate module.
- Use the i18n translation system for all user-facing text.
- Ensure each file ends with a newline and avoid trailing whitespace.

## Testing and Coverage
- Achieve **at least 80% line coverage** on new or modified code.
- Run `./gradlew codeCoverageReport` and inspect `build/reports/jacoco` before opening a PR.
 - For new gameplay mechanisms, add a scenario test using `GameSimulation` in the `tests` module.
 - When modifying the rendering system, rerun the benchmarks described in `docs/performance.md` and compare the results
    to the previous run. Avoid introducing changes that make benchmarks slower. Whenever results change, update
    `docs/performance.md` with the new numbers so future runs have an accurate baseline.

## Save Format and Serialization
Whenever a PR changes save formats or Kryo serialization, add a new save version and migration.
Each PR must introduce its own migration. Follow these steps:
1. Add the next constant in `SaveVersion`.
2. Implement migration logic in `SaveMigrator`.
3. Add tests covering the migration.
4. Update the Kryo registration hash to detect mismatches.

## Commit and Release Guidelines
- All commit messages and PR titles must follow the [Angular commit message guidelines](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#commit).
- Use `feat:` for new features and `fix:` for bug fixes.
- If the change is breaking for users, include `BREAKING CHANGE:` in the commit footer.
- Semantic Release uses commit messages to determine version bumps and generate release notes.

## Networking Workflow
Client-driven actions follow this sequence:
1. The client sends a request message to the server.
2. The server validates and applies the change, then broadcasts the result.
3. Clients queue incoming updates and apply them during their normal update loop.

Do not apply world changes locally until the server response is processed so every client stays in sync.

## Helpful Resources
- Consult `README.md` for quick start instructions.
- See the guides under `docs/` for architecture, networking and configuration details.
- Each module's `AGENTS.md` provides additional module specific notes.

If any command fails during checks or tests, resolve the issue before committing.
