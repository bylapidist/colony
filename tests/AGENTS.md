# Purpose
JUnit tests and utilities including `GdxTestRunner` for headless LibGDX execution.

# Key tasks
- Provide scenario tests using `GameSimulation` for gameplay validation.
- Copy assets from the client module via `./gradlew tests:copyAssets` before running tests.

# Testing notes
- Run `./gradlew clean test` followed by `./gradlew check` to ensure style and coverage.
