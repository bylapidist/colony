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

## Notes
The project uses Java 21 toolchains and standard Gradle tasks. No additional tools are required beyond what is defined in the Gradle build.

Semantic Release automatically increments versions and generates GitHub release notes from commit messages. Provide a concise description of your changes in the PR body so release notes remain meaningful.

When changing or adding new functionality, ensure it has adequate test coverage.

If any command fails, fix the issues before committing.

## Commit Messages
All commit messages must follow the [Angular commit message guidelines](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#commit).

When creating PRs make sure the PR title also follows this convention.
