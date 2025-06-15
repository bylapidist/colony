package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Identity migration for save version 28 to 29. */
public final class V28ToV29Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V28.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V29.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder().version(toVersion()).build();
    }
}
