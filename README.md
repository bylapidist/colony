### Colony
[![Java CI](https://github.com/bylapidist/colony/actions/workflows/gradle.yml/badge.svg)](https://github.com/bylapidist/colony/actions/workflows/gradle.yml)

## Building

This project uses Gradle with Java 21 toolchains. Run the following commands to format the code, copy test assets and execute the test suite:

```bash
./gradlew tests:copyAssets
./gradlew spotlessApply
./gradlew clean test
```

## Running

With the `application` plugin applied, the client and server can be started directly from Gradle:

```bash
./gradlew :client:run   # start the game client
./gradlew :server:run   # start the dedicated server
```
