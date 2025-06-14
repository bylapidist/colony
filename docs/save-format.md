# Save Format

This guide explains how to update the save game version whenever the serialized state changes.

1. Add the next constant in `SaveVersion` and update `CURRENT` to the new value.
2. Implement a `MapStateMigration` that converts the old data to the new structure.
3. Register the migration in `SaveMigrator` so it is applied during loading.
4. Run `SerializationRegistrar.registrationHash()` and set `REGISTRATION_HASH` to the new value.
5. Write tests covering the migration to ensure old saves load correctly.
