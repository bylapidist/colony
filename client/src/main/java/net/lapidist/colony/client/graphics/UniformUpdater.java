package net.lapidist.colony.client.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Allows a shader plugin to provide per-frame uniform updates.
 */
public interface UniformUpdater {
    /**
     * Apply uniform values to the provided shader program.
     *
     * @param program currently bound shader
     */
    void applyUniforms(ShaderProgram program);
}
