#!/usr/bin/env bash
set -euo pipefail

./gradlew tests:copyAssets
./gradlew spotlessApply
./gradlew clean test
./gradlew check
javac scripts/I18nCheck.java
java -cp scripts I18nCheck
