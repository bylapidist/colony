package net.lapidist.colony.client.systems.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.KeyBindings;

/**
 * Handles keyboard based camera movement.
 */
public final class KeyboardInputHandler {

    private static final float CAMERA_SPEED = 400f; // units per second

    private final PlayerCameraSystem cameraSystem;
    private final KeyBindings keyBindings;
    private final net.lapidist.colony.client.network.GameClient client;

    public KeyboardInputHandler(
            final PlayerCameraSystem cameraSystemToSet,
            final KeyBindings bindings
    ) {
        this(null, cameraSystemToSet, bindings);
    }

    public KeyboardInputHandler(
            final net.lapidist.colony.client.network.GameClient clientToUse,
            final PlayerCameraSystem cameraSystemToSet,
            final KeyBindings bindings
    ) {
        this.client = clientToUse;
        this.cameraSystem = cameraSystemToSet;
        this.keyBindings = bindings;
    }

    public void handleKeyboardInput(final float deltaTime) {
        if (cameraSystem.isPlayerMode()) {
            return;
        }
        final float moveAmount = CAMERA_SPEED * deltaTime;
        float dx = 0f;
        float dy = 0f;

        if (Gdx.input.isKeyPressed(keyBindings.getKey(KeyAction.MOVE_UP))) {
            dy += moveAmount;
        }
        if (Gdx.input.isKeyPressed(keyBindings.getKey(KeyAction.MOVE_DOWN))) {
            dy -= moveAmount;
        }
        if (Gdx.input.isKeyPressed(keyBindings.getKey(KeyAction.MOVE_LEFT))) {
            dx -= moveAmount;
        }
        if (Gdx.input.isKeyPressed(keyBindings.getKey(KeyAction.MOVE_RIGHT))) {
            dx += moveAmount;
        }

        if (dx != 0f || dy != 0f) {
            cameraSystem.translate(dx, dy);
        }
    }

    public void clampCameraPosition() {
        if (cameraSystem.isPlayerMode()) {
            return;
        }
        final com.badlogic.gdx.math.Vector2 position = cameraSystem.getTargetPosition();
        float width = client != null ? client.getMapWidth() : MapState.DEFAULT_WIDTH;
        float height = client != null ? client.getMapHeight() : MapState.DEFAULT_HEIGHT;
        final float mapWidth = width * GameConstants.TILE_SIZE;
        final float mapHeight = height * GameConstants.TILE_SIZE;

        var camera = (OrthographicCamera) cameraSystem.getCamera();
        var viewport = (ExtendViewport) cameraSystem.getViewport();

        float halfWidth = Math.min(viewport.getWorldWidth() * camera.zoom / 2f, mapWidth / 2f);
        float halfHeight = Math.min(viewport.getWorldHeight() * camera.zoom / 2f, mapHeight / 2f);

        position.x = MathUtils.clamp(position.x, halfWidth, mapWidth - halfWidth);
        position.y = MathUtils.clamp(position.y, halfHeight, mapHeight - halfHeight);
        cameraSystem.setTargetPosition(position);
    }
}
