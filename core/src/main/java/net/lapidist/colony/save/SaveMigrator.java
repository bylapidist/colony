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
        register(new V3ToV4Migration());
        register(new V4ToV5Migration());
        register(new V5ToV6Migration());
        register(new V6ToV7Migration());
        register(new V7ToV8Migration());
        register(new V8ToV9Migration());
        register(new V9ToV10Migration());
        register(new V10ToV11Migration());
        register(new V11ToV12Migration());
        register(new V12ToV13Migration());
        register(new V13ToV14Migration());
        register(new V14ToV15Migration());
        register(new V15ToV16Migration());
        register(new V16ToV17Migration());
        register(new V17ToV18Migration());
        register(new V18ToV19Migration());
        register(new V19ToV20Migration());
        register(new V20ToV21Migration());
        register(new V21ToV22Migration());
        register(new V22ToV23Migration());
        register(new V23ToV24Migration());
        register(new V24ToV25Migration());
        register(new V25ToV26Migration());
        register(new V26ToV27Migration());
        register(new V27ToV28Migration());
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
