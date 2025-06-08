# Contributing to Colony

Thank you for your interest in contributing to Colony! This project follows the [Angular commit message guidelines](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#commit). All pull requests should use this style for commits and titles so releases can be generated correctly.

## Development Setup

1. Install Java 21 and ensure `./gradlew` is executable.
2. Before running the tests, execute:
   ```bash
   ./gradlew tests:copyAssets
   ```
3. Format the code and run the test suite with:
   ```bash
   ./gradlew spotlessApply clean test check
   ```

## Submitting Changes

- Fork the repository and create a feature branch.
- Ensure all tests pass and no Checkstyle violations remain.
- Add new tests when introducing functionality or modifying behaviour.
- Open a pull request describing your changes.

By contributing to this project you agree that your contributions are licensed under the MIT License.
