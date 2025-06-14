# Save Format

Game state is serialized into a `SaveData` record containing the save version, a Kryo registration hash and the `MapState`. Files are written and read via `GameStateIO`.

## SaveVersion

`SaveVersion` enumerates each supported format and exposes `CURRENT` for the newest one. The version number is stored in every save so old files can be migrated.

## SaveMigrator Workflow

`DefaultSaveMigrationStrategy` checks the file version when loading a save. If it is older than `SaveVersion.CURRENT`, it calls `SaveMigrator.migrate`. `SaveMigrator` applies each `MapStateMigration` step in order and returns an updated `MapState` with the target version.

The repository's root `AGENTS.md` outlines the required steps whenever the save format changes:

1. Add the next constant in `SaveVersion`.
2. Implement migration logic in `SaveMigrator`.
3. Add tests covering the migration.
4. Update the Kryo registration hash to detect mismatches.

