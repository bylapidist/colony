package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Identity migration for save version 31 to 32. */
public final class V31ToV32Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V31.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V32.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder().version(toVersion()).build();
    }
}
