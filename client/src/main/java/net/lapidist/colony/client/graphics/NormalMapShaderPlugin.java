package net.lapidist.colony.client.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.lapidist.colony.client.core.io.FileLocation;

import java.io.IOException;

/** Shader plugin blending diffuse, normal and specular maps. */
public final class NormalMapShaderPlugin implements ShaderPlugin {
    @Override
    public ShaderProgram create(final ShaderManager manager) {
        try {
            return manager.load(FileLocation.INTERNAL,
                    "shaders/normal.vert", "shaders/normal.frag");
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String id() {
        return "normalmap";
    }

    @Override
    public String displayName() {
        return "Normal Mapping";
    }
}
