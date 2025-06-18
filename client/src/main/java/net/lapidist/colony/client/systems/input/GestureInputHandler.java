package net.lapidist.colony.client.systems.input;

import com.badlogic.gdx.math.MathUtils;
import net.lapidist.colony.client.systems.PlayerCameraSystem;

/**
 * Handles gesture based camera interactions.
 */
public final class GestureInputHandler {

    private static final float ZOOM_SPEED = 0.02f;
    private static final float MIN_ZOOM = 0.5f;

    private final PlayerCameraSystem cameraSystem;

    public GestureInputHandler(final PlayerCameraSystem cameraSystemToSet) {
        this.cameraSystem = cameraSystemToSet;
    }

    public boolean scrolled(final float amountX, final float amountY) {
        float zoom = cameraSystem.getZoom() + amountY * ZOOM_SPEED;
        cameraSystem.setZoom(MathUtils.clamp(zoom, MIN_ZOOM, cameraSystem.getMaxZoom()));
        return true;
    }

    public boolean pan(final float deltaX, final float deltaY) {
        float factor = cameraSystem.getZoom();
        cameraSystem.translate(-deltaX * factor, deltaY * factor);
        return true;
    }

    public boolean zoom(final float initialDistance, final float distance) {
        final float ratio = initialDistance / distance;
        float zoom = cameraSystem.getZoom() * ratio;
        cameraSystem.setZoom(MathUtils.clamp(zoom, MIN_ZOOM, cameraSystem.getMaxZoom()));
        return true;
    }
}
