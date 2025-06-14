package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 16 to 17 with no data changes. */
public final class V16ToV17Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V16.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V17.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
