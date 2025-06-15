package net.lapidist.colony.client.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Extension point for providing shader programs.
 */
public interface ShaderPlugin {

    /**
     * Create or load a shader program.
     *
     * @param manager shader manager used for compilation
     * @return created program or {@code null} if not available
     */
    ShaderProgram create(ShaderManager manager);

    /**
     * Dispose any resources used by this plugin.
     */
    default void dispose() {
    }
}
