package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 20 to 21 with no data changes. */
public final class V20ToV21Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V20.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V21.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
