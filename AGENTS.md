This repository contains a multi-module Java project built with Gradle. It is
split into four modules:

- **core** – shared game logic, ECS components and serializers
- **client** – the desktop LibGDX client
- **server** – the headless game server
- **tests** – JUnit test utilities and suites

## Running checks
- Always run `./gradlew tests:copyAssets` before running the normal test suite. This copies required assets for the test module.
- Run `./gradlew spotlessApply` before committing to automatically format the code.
- After copying the assets run `./gradlew clean test` to execute all tests.
- Run `./gradlew check` to verify code style. Only commit if this task succeeds. It includes Checkstyle and Spotless checks defined in `config/checkstyle/checkstyle.xml`.

## Running the game
- Use `./gradlew :client:run` to start the desktop client.
- Use `./gradlew :server:run` to start the dedicated server.

## Coding conventions
- All Java source files use four space indentation and must not contain tab characters.
- Keep lines under 120 characters as configured in Checkstyle.
- Ensure each file ends with a newline and avoid trailing whitespace.
- New classes should be placed under the `net.lapidist.colony` package in the
  module that matches their purpose (`core`, `client`, `server`, or `tests`).
- Use the i18n translation system for all user-facing text. Never hardcode
  strings directly in the code.

## Notes
The project uses Java 21 toolchains and standard Gradle tasks. No additional tools are required beyond what is defined in the Gradle build.

Semantic Release automatically increments versions and generates GitHub release notes from commit messages. Provide a concise description of your changes in the PR body so release notes remain meaningful.

When changing or adding new functionality, ensure it has adequate test coverage.
When modifying or introducing new gameplay mechanisms, write a scenario test
using the `GameSimulation` utility found under the `tests` module to verify the
behaviour.

When introducing a new save format or modifying Kryo serialization, you must:
1. Define the next constant in `SaveVersion`.
2. Implement a migration from the previous version in `SaveMigrator`.
3. Add tests covering the migration.
4. Update the Kryo registration hash so version mismatches are detected.

If any command fails, fix the issues before committing.

## Commit Messages
All commit messages must follow the [Angular commit message guidelines](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#commit).

When creating PRs make sure the PR title also follows this convention.

## Networking workflow
Client driven actions that modify the game world must follow this sequence:

1. The client sends a request message to the server describing the action.
2. The server processes the request and broadcasts the resulting state change to all clients.
3. Clients queue incoming updates and apply them in a system during their normal update loop.

Do not apply world state changes locally until the server response is received and processed. This ensures all clients stay in sync.
