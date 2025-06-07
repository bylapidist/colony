This repository contains a multi-module Java project built with Gradle.

## Running checks
- Always run `./gradlew tests:copyAssets` before running the normal test suite. This copies required assets for the test module.
- After copying the assets run `./gradlew clean test` to execute all tests.
- To verify code style run `./gradlew check`, which includes Checkstyle checks defined in `config/checkstyle/checkstyle.xml`.

## Coding conventions
- All Java source files use four space indentation and must not contain tab characters.
- Keep lines under 120 characters as configured in Checkstyle.
- Ensure each file ends with a newline and avoid trailing whitespace.
- New classes should be placed under the `net.lapidist.colony` package in the appropriate module (`components`, `client`, or `tests`).

## Notes
The project uses Java 21 toolchains and standard Gradle tasks. No additional tools are required beyond what is defined in the Gradle build.

Semantic Release automatically increments versions and generates GitHub release notes from commit messages. Provide a concise description of your changes in the PR body so release notes remain meaningful.

If any command fails, fix the issues before committing.

## Commit Messages
All commit messages must follow the [Angular commit message guidelines](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#commit).

When creating PRs make sure the PR title also follows this convention.
