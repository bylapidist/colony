package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 21 to 22 with no data changes. */
public final class V21ToV22Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V21.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V22.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
