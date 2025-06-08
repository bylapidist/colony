package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

import java.util.EnumMap;
import java.util.Map;

/**
 * Applies deterministic migrations between different save versions.
 */
public final class SaveMigrator {

    private static final Map<SaveVersion, MapStateMigration> MIGRATIONS = new EnumMap<>(SaveVersion.class);

    static {
        register(new V1ToV2Migration());
        register(new V2ToV3Migration());
    }

    private SaveMigrator() {
    }

    private static void register(final MapStateMigration migration) {
        MIGRATIONS.put(SaveVersion.fromNumber(migration.fromVersion()), migration);
    }

    /**
     * Migrates the supplied {@link MapState} from {@code fromVersion} to {@code toVersion}.
     */
    public static MapState migrate(final MapState data, final int fromVersion, final int toVersion) {
        MapState result = data;
        for (int v = fromVersion; v < toVersion; v++) {
            MapStateMigration migration = MIGRATIONS.get(SaveVersion.fromNumber(v));
            if (migration == null) {
                throw new IllegalStateException("Missing migration step for version " + v);
            }
            result = migration.apply(result);
        }
        return result.toBuilder()
                .version(toVersion)
                .build();
    }
}
