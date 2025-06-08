package net.lapidist.colony.client.systems.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.client.systems.PlayerCameraSystem;

/**
 * Handles camera movement and zoom interactions.
 */
public final class CameraInputHandler {

    private static final float CAMERA_SPEED = 400f; // units per second
    private static final float ZOOM_SPEED = 0.02f;
    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 2f;

    private final PlayerCameraSystem cameraSystem;

    public CameraInputHandler(final PlayerCameraSystem cameraSystemToSet) {
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

    public boolean scrolled(final float amountX, final float amountY) {
        final float zoom = cameraSystem.getCamera().zoom + amountY * ZOOM_SPEED;
        cameraSystem.getCamera().zoom = MathUtils.clamp(zoom, MIN_ZOOM, MAX_ZOOM);
        cameraSystem.getCamera().update();
        return true;
    }

    public boolean pan(final float deltaX, final float deltaY) {
        cameraSystem.getCamera().translate(
                -deltaX * cameraSystem.getCamera().zoom,
                deltaY * cameraSystem.getCamera().zoom,
                0
        );
        clampCameraPosition();
        cameraSystem.getCamera().update();
        return true;
    }

    public boolean zoom(final float initialDistance, final float distance) {
        final float ratio = initialDistance / distance;
        final float zoom = cameraSystem.getCamera().zoom * ratio;
        cameraSystem.getCamera().zoom = MathUtils.clamp(zoom, MIN_ZOOM, MAX_ZOOM);
        cameraSystem.getCamera().update();
        return true;
    }
}
