package net.lapidist.colony.client.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Default implementation that provides no shader program.
 */
public final class NullShaderPlugin implements ShaderPlugin {
    @Override
    public ShaderProgram create(final ShaderManager manager) {
        return null;
    }
}
