package net.lapidist.colony.client.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * Provides access to the game camera and viewport for rendering systems.
 */
public interface CameraProvider {
    /** Returns the active orthographic camera. */
    OrthographicCamera getCamera();

    /** Returns the viewport associated with the camera. */
    ExtendViewport getViewport();
}
