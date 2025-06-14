package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Identity migration for save version 25 to 26. */
public final class V25ToV26Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V25.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V26.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder().version(toVersion()).build();
    }
}
