package net.lapidist.colony.client.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Provides access to the game camera and viewport for rendering systems.
 */
public interface CameraProvider {
    /** Returns the active camera. */
    Camera getCamera();

    /** Returns the viewport associated with the camera. */
    Viewport getViewport();

    /** Returns visible tile bounds in tile coordinates. */
    com.badlogic.gdx.math.Rectangle getVisibleTileBounds();
}
