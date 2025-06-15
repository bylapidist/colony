package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.utils.Disposable;

/**
 * Handles post-processing effects using framebuffers.
 * Implementations capture the rendered scene and
 * apply additional passes before presenting the final
 * image on screen.
 */
public interface PostProcessor extends Disposable {

    /**
     * Resize internal buffers.
     *
     * @param width  viewport width
     * @param height viewport height
     */
    void resize(int width, int height);

    /**
     * Begin capturing the scene to a framebuffer.
     */
    void begin();

    /**
     * Apply the effect and output to the back buffer.
     */
    void end();
}
