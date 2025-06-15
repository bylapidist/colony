# Developer Workflow

This guide summarizes the common build and test steps for all modules.

## Build and Style
1. `./gradlew tests:copyAssets` – copy client assets into the test module.
2. `./gradlew spotlessApply` – automatically format the source code.
3. `./gradlew clean test` – compile and run all tests.
4. `./gradlew check` – run Checkstyle and Spotless verification.
5. `./gradlew codeCoverageReport` – generate the JaCoCo coverage report.

Running `./scripts/check.sh` performs the first four steps at once.

## Coverage
New or modified code should maintain **at least 80% line coverage**. After running the coverage task, open
`build/reports/jacoco/test/html/index.html` to inspect the results.

## Save Format Migrations
Whenever a change affects the serialized save format:
1. Add the next constant in `SaveVersion`.
2. Implement the migration in `SaveMigrator`.
3. Write tests covering the migration.
4. Update the Kryo registration hash.

Documentation-only changes do not require a migration.
