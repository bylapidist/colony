package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 17 to 18 with no data changes. */
public final class V17ToV18Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V17.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V18.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
