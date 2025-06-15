# Purpose
Shared ECS components, constants and serialization code used by the client and server modules.

# Key tasks
- Implement cross-platform game logic and common data classes here.
- Maintain Kryo registration consistency across versions.

# Testing notes
- Run `./gradlew tests:copyAssets` before executing the main test suite.
- Execute `./gradlew clean test` and `./gradlew check` to verify changes.
- Run `./gradlew codeCoverageReport` to generate coverage data.
