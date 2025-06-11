package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/**
 * Migration from save version 2 to version 3.
 */
public final class V2ToV3Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V2.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V3.number();
    }

    @Override
    public MapState apply(final MapState state) {
        // No structural changes between v2 and v3, just bump the version.
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
