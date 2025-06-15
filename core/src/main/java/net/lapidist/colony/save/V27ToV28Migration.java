package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Identity migration for save version 27 to 28. */
public final class V27ToV28Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V27.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V28.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder().version(toVersion()).build();
    }
}
