#!/usr/bin/env bash
set -euo pipefail

./gradlew tests:copyAssets
./gradlew spotlessApply
./gradlew clean test
./gradlew check
