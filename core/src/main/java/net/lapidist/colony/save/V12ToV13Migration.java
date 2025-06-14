package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 12 to 13 with no data changes. */
public final class V12ToV13Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V12.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V13.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
