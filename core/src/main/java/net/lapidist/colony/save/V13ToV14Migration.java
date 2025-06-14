package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 13 to 14 adding map dimensions. */
public final class V13ToV14Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V13.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V14.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .width(MapState.DEFAULT_WIDTH)
                .height(MapState.DEFAULT_HEIGHT)
                .version(toVersion())
                .build();
    }
}
