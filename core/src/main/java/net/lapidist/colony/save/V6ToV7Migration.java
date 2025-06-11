package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 6 to version 7 for kryo registration change. */
public final class V6ToV7Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V6.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V7.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
