package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Identity migration for save version 32 to 33. */
public final class V32ToV33Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V32.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V33.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder().version(toVersion()).build();
    }
}
