package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.PlayerPosition;

/** Migration from save version 7 to version 8 adding player position. */
public final class V7ToV8Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V7.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V8.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .playerPos(new PlayerPosition(
                        MapState.DEFAULT_WIDTH / 2,
                        MapState.DEFAULT_HEIGHT / 2))
                .version(toVersion())
                .build();
    }
}
