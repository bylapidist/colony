package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Identity migration for save version 29 to 30. */
public final class V29ToV30Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V29.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V30.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder().version(toVersion()).build();
    }
}
