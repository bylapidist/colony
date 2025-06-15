package net.lapidist.colony.client.graphics;

import box2dLight.RayHandler;

/**
 * Marker interface for shader plugins that also provide lighting support.
 */
public interface LightingPlugin extends ShaderPlugin {

    /** Return the initialized {@link RayHandler} or {@code null} if unavailable. */
    RayHandler getRayHandler();
}
