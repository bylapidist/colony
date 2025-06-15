package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Identity migration for save version 33 to 34. */
public final class V33ToV34Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V33.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V34.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder().version(toVersion()).build();
    }
}
