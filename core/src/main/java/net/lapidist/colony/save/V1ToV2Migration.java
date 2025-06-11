package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/**
 * Migration from save version 1 to version 2.
 */
public final class V1ToV2Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V1.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V2.number();
    }

    @Override
    public MapState apply(final MapState state) {
        // No structural changes between v1 and v2, just bump the version.
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
