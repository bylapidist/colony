package net.lapidist.colony.client.systems.input;

import com.badlogic.gdx.math.MathUtils;
import net.lapidist.colony.client.systems.PlayerCameraSystem;

/**
 * Handles gesture based camera interactions.
 */
public final class GestureInputHandler {

    private static final float ZOOM_SPEED = 0.02f;
    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 2f;

    private final PlayerCameraSystem cameraSystem;

    public GestureInputHandler(final PlayerCameraSystem cameraSystemToSet) {
        this.cameraSystem = cameraSystemToSet;
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
