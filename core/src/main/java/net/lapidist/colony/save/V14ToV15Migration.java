package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 14 to 15 with no data changes. */
public final class V14ToV15Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V14.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V15.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
