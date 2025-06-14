package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 19 to 20 with no data changes. */
public final class V19ToV20Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V19.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V20.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
