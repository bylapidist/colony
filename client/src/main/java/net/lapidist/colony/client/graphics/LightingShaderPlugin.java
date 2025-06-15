package net.lapidist.colony.client.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import java.io.IOException;

/** Shader plugin providing simple 2D lighting. */
public final class LightingShaderPlugin implements ShaderPlugin {
    @Override
    public ShaderProgram create(final ShaderManager manager) {
        try {
            return manager.load(
                    net.lapidist.colony.client.core.io.FileLocation.INTERNAL,
                    "shaders/lighting.vert",
                    "shaders/lighting.frag"
            );
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String id() {
        return "lighting";
    }

    @Override
    public String displayName() {
        return "Lighting";
    }
}
