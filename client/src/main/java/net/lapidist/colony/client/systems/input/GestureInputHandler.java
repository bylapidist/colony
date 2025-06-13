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
        var cam = (com.badlogic.gdx.graphics.OrthographicCamera) cameraSystem.getCamera();
        final float zoom = cam.zoom + amountY * ZOOM_SPEED;
        cam.zoom = MathUtils.clamp(zoom, MIN_ZOOM, cameraSystem.getMaxZoom());
        cam.update();
        return true;
    }

    public boolean pan(final float deltaX, final float deltaY) {
        var cam = (com.badlogic.gdx.graphics.OrthographicCamera) cameraSystem.getCamera();
        cam.translate(
                -deltaX * cam.zoom,
                deltaY * cam.zoom,
                0
        );
        cam.update();
        return true;
    }

    public boolean zoom(final float initialDistance, final float distance) {
        var cam = (com.badlogic.gdx.graphics.OrthographicCamera) cameraSystem.getCamera();
        final float ratio = initialDistance / distance;
        final float zoom = cam.zoom * ratio;
        cam.zoom = MathUtils.clamp(zoom, MIN_ZOOM, cameraSystem.getMaxZoom());
        cam.update();
        return true;
    }
}
