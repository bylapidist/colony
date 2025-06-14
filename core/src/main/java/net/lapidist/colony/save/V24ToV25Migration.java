package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Identity migration for save version 24 to 25. */
public final class V24ToV25Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V24.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V25.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder().version(toVersion()).build();
    }
}
