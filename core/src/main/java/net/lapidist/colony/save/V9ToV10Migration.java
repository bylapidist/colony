package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 9 to 10 with no structural changes. */
public final class V9ToV10Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V9.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V10.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder().version(toVersion()).build();
    }
}
