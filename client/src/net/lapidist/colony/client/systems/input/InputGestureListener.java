package net.lapidist.colony.client.systems.input;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
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
    private final Entity map;
    private final ComponentMapper<MapComponent> mapMapper;
    private final ComponentMapper<TileComponent> tileMapper;

    public InputGestureListener(
            final GestureInputHandler gestureHandlerToSet,
            final KeyboardInputHandler keyboardHandlerToSet,
            final TileSelectionHandler tileSelectionHandlerToSet,
            final Entity mapToSet,
            final ComponentMapper<MapComponent> mapMapperToSet,
            final ComponentMapper<TileComponent> tileMapperToSet
    ) {
        this.gestureHandler = gestureHandlerToSet;
        this.keyboardHandler = keyboardHandlerToSet;
        this.tileSelectionHandler = tileSelectionHandlerToSet;
        this.map = mapToSet;
        this.mapMapper = mapMapperToSet;
        this.tileMapper = tileMapperToSet;
    }

    @Override
    public boolean tap(final float x, final float y, final int count, final int button) {
        return tileSelectionHandler.handleTap(x, y, map, mapMapper, tileMapper);
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
