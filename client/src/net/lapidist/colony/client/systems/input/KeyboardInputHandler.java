package net.lapidist.colony.client.systems.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.client.systems.PlayerCameraSystem;

/**
 * Handles keyboard based camera movement.
 */
public final class KeyboardInputHandler {

    private static final float CAMERA_SPEED = 400f; // units per second

    private final PlayerCameraSystem cameraSystem;

    public KeyboardInputHandler(final PlayerCameraSystem cameraSystemToSet) {
        this.cameraSystem = cameraSystemToSet;
    }

    public void handleKeyboardInput(final float deltaTime) {
        final float moveAmount = CAMERA_SPEED * deltaTime;
        final Vector3 position = cameraSystem.getCamera().position;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.y += moveAmount;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.y -= moveAmount;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            position.x -= moveAmount;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            position.x += moveAmount;
        }
    }

    public void clampCameraPosition() {
        final Vector3 position = cameraSystem.getCamera().position;
        final float mapWidth = Constants.MAP_WIDTH * Constants.TILE_SIZE;
        final float mapHeight = Constants.MAP_HEIGHT * Constants.TILE_SIZE;

        position.x = MathUtils.clamp(position.x, 0, mapWidth);
        position.y = MathUtils.clamp(position.y, 0, mapHeight);
    }
}
