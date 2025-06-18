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
        register(new VersionBumpMigration(SaveVersion.V1, SaveVersion.V2));
        register(new VersionBumpMigration(SaveVersion.V2, SaveVersion.V3));
        register(new V3ToV4Migration());
        register(new V4ToV5Migration());
        register(new VersionBumpMigration(SaveVersion.V5, SaveVersion.V6));
        register(new V6ToV7Migration());
        register(new V7ToV8Migration());
        register(new VersionBumpMigration(SaveVersion.V8, SaveVersion.V9));
        register(new VersionBumpMigration(SaveVersion.V9, SaveVersion.V10));
        register(new V10ToV11Migration());
        register(new VersionBumpMigration(SaveVersion.V11, SaveVersion.V12));
        register(new VersionBumpMigration(SaveVersion.V12, SaveVersion.V13));
        register(new V13ToV14Migration());
        register(new VersionBumpMigration(SaveVersion.V14, SaveVersion.V15));
        register(new VersionBumpMigration(SaveVersion.V15, SaveVersion.V16));
        register(new VersionBumpMigration(SaveVersion.V16, SaveVersion.V17));
        register(new VersionBumpMigration(SaveVersion.V17, SaveVersion.V18));
        register(new VersionBumpMigration(SaveVersion.V18, SaveVersion.V19));
        register(new VersionBumpMigration(SaveVersion.V19, SaveVersion.V20));
        register(new VersionBumpMigration(SaveVersion.V20, SaveVersion.V21));
        register(new VersionBumpMigration(SaveVersion.V21, SaveVersion.V22));
        register(new V22ToV23Migration());
        register(new V23ToV24Migration());
        register(new VersionBumpMigration(SaveVersion.V24, SaveVersion.V25));
        register(new VersionBumpMigration(SaveVersion.V25, SaveVersion.V26));
        register(new VersionBumpMigration(SaveVersion.V26, SaveVersion.V27));
        register(new VersionBumpMigration(SaveVersion.V27, SaveVersion.V28));
        register(new VersionBumpMigration(SaveVersion.V28, SaveVersion.V29));
        register(new VersionBumpMigration(SaveVersion.V29, SaveVersion.V30));
        register(new VersionBumpMigration(SaveVersion.V30, SaveVersion.V31));
        register(new VersionBumpMigration(SaveVersion.V31, SaveVersion.V32));
        register(new VersionBumpMigration(SaveVersion.V32, SaveVersion.V33));
        register(new VersionBumpMigration(SaveVersion.V33, SaveVersion.V34));
        register(new V34ToV35Migration());
        register(new V35ToV36Migration());
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
