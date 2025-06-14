package net.lapidist.colony.client.systems.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.lapidist.colony.components.GameConstants;
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
        final Vector3 position = cameraSystem.getCamera().position;

        if (Gdx.input.isKeyPressed(keyBindings.getKey(KeyAction.MOVE_UP))) {
            position.y += moveAmount;
        }
        if (Gdx.input.isKeyPressed(keyBindings.getKey(KeyAction.MOVE_DOWN))) {
            position.y -= moveAmount;
        }
        if (Gdx.input.isKeyPressed(keyBindings.getKey(KeyAction.MOVE_LEFT))) {
            position.x -= moveAmount;
        }
        if (Gdx.input.isKeyPressed(keyBindings.getKey(KeyAction.MOVE_RIGHT))) {
            position.x += moveAmount;
        }
    }

    public void clampCameraPosition() {
        if (cameraSystem.isPlayerMode()) {
            return;
        }
        final Vector3 position = cameraSystem.getCamera().position;
        float width = client != null ? client.getMapWidth() : GameConstants.MAP_WIDTH;
        float height = client != null ? client.getMapHeight() : GameConstants.MAP_HEIGHT;
        final float mapWidth = width * GameConstants.TILE_SIZE;
        final float mapHeight = height * GameConstants.TILE_SIZE;

        var camera = (OrthographicCamera) cameraSystem.getCamera();
        var viewport = (ExtendViewport) cameraSystem.getViewport();

        float halfWidth = Math.min(viewport.getWorldWidth() * camera.zoom / 2f, mapWidth / 2f);
        float halfHeight = Math.min(viewport.getWorldHeight() * camera.zoom / 2f, mapHeight / 2f);

        position.x = MathUtils.clamp(position.x, halfWidth, mapWidth - halfWidth);
        position.y = MathUtils.clamp(position.y, halfHeight, mapHeight - halfHeight);
    }
}
