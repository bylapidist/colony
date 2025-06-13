package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 8 to 9 with no structural changes. */
public final class V8ToV9Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V8.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V9.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder().version(toVersion()).build();
    }
}
