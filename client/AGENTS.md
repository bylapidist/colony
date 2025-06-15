# Purpose
Desktop LibGDX client providing the game loop, rendering and user interface.

# Key tasks
- Integrate networking code to communicate with the server.
- Run the game locally with `./gradlew :client:run`.

# Testing notes
- Ensure assets remain under the `assets` directory for packaging.
- Full build and style checks are triggered via `./gradlew clean test check`.
- Use `./gradlew codeCoverageReport` to review test coverage.
