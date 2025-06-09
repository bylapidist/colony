package net.lapidist.colony.client.systems.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
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

    public KeyboardInputHandler(
            final PlayerCameraSystem cameraSystemToSet,
            final KeyBindings bindings
    ) {
        this.cameraSystem = cameraSystemToSet;
        this.keyBindings = bindings;
    }

    public void handleKeyboardInput(final float deltaTime) {
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
        final Vector3 position = cameraSystem.getCamera().position;
        final float mapWidth = GameConstants.MAP_WIDTH * GameConstants.TILE_SIZE;
        final float mapHeight = GameConstants.MAP_HEIGHT * GameConstants.TILE_SIZE;

        position.x = MathUtils.clamp(position.x, 0, mapWidth);
        position.y = MathUtils.clamp(position.y, 0, mapHeight);
    }
}
