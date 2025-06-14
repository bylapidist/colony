package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 18 to 19 with no data changes. */
public final class V18ToV19Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V18.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V19.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
