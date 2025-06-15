package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Identity migration for save version 26 to 27. */
public final class V26ToV27Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V26.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V27.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder().version(toVersion()).build();
    }
}
