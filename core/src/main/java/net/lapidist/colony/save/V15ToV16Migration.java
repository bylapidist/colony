package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 15 to 16 with no data changes. */
public final class V15ToV16Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V15.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V16.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
