package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Identity migration for save version 30 to 31. */
public final class V30ToV31Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V30.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V31.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder().version(toVersion()).build();
    }
}
