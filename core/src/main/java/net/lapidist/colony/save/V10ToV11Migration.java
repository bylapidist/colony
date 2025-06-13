package net.lapidist.colony.save;

import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.CameraPosition;
import net.lapidist.colony.components.state.MapState;

/** Migration from save version 10 to 11 adding camera position. */
public final class V10ToV11Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V10.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V11.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .cameraPos(new CameraPosition(
                        GameConstants.MAP_WIDTH / 2f,
                        GameConstants.MAP_HEIGHT / 2f))
                .version(toVersion())
                .build();
    }
}
