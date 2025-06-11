package net.lapidist.colony.client.systems.input;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;

/**
 * Dispatches gesture events to the appropriate handlers.
 */
public final class InputGestureListener extends GestureAdapter {

    private final GestureInputHandler gestureHandler;
    private final KeyboardInputHandler keyboardHandler;
    private final TileSelectionHandler tileSelectionHandler;
    private MapComponent map;
    private final ComponentMapper<TileComponent> tileMapper;

    public InputGestureListener(
            final GestureInputHandler gestureHandlerToSet,
            final KeyboardInputHandler keyboardHandlerToSet,
            final TileSelectionHandler tileSelectionHandlerToSet,
            final MapComponent mapToSet,
            final ComponentMapper<TileComponent> tileMapperToSet
    ) {
        this.gestureHandler = gestureHandlerToSet;
        this.keyboardHandler = keyboardHandlerToSet;
        this.tileSelectionHandler = tileSelectionHandlerToSet;
        this.map = mapToSet;
        this.tileMapper = tileMapperToSet;
    }

    public void setMap(final MapComponent mapToSet) {
        this.map = mapToSet;
    }

    @Override
    public boolean tap(final float x, final float y, final int count, final int button) {
        return tileSelectionHandler.handleTap(x, y, map, tileMapper);
    }

    @Override
    public boolean pan(final float x, final float y, final float deltaX, final float deltaY) {
        boolean result = gestureHandler.pan(deltaX, deltaY);
        keyboardHandler.clampCameraPosition();
        return result;
    }

    @Override
    public boolean zoom(final float initialDistance, final float distance) {
        return gestureHandler.zoom(initialDistance, distance);
    }
}
