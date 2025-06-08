package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Applies deterministic migrations between different save versions.
 */
public final class SaveMigrator {

    private static final Map<Integer, UnaryOperator<MapState>> MIGRATIONS = new HashMap<>();

    static {
        MIGRATIONS.put(SaveVersion.V1.number(), SaveMigrator::v1ToV2);
    }

    private SaveMigrator() {
    }

    private static MapState v1ToV2(final MapState state) {
        // No structural changes between v1 and v2, just bump the version.
        return state.toBuilder()
                .version(SaveVersion.V2.number())
                .build();
    }

    /**
     * Migrates the supplied {@link MapState} from {@code fromVersion} to {@code toVersion}.
     */
    public static MapState migrate(final MapState data, final int fromVersion, final int toVersion) {
        MapState result = data;
        for (int v = fromVersion; v < toVersion; v++) {
            UnaryOperator<MapState> op = MIGRATIONS.get(v);
            if (op == null) {
                throw new IllegalStateException("Missing migration step for version " + v);
            }
            result = op.apply(result);
        }
        return result.toBuilder()
                .version(toVersion)
                .build();
    }
}
