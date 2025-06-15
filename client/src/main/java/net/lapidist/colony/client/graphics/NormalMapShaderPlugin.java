package net.lapidist.colony.client.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.lapidist.colony.client.core.io.FileLocation;

import java.io.IOException;

/**
 * Shader plugin blending diffuse, normal and specular maps.
 */
public final class NormalMapShaderPlugin implements ShaderPlugin {
    @Override
    public ShaderProgram create(final ShaderManager manager) {
        try {
            ShaderProgram program = manager.load(FileLocation.INTERNAL,
                    "shaders/normal.vert", "shaders/normal.frag");
            program.begin();
            program.setUniformi("u_texture", 0);
            program.setUniformi("u_normal", 1);
            program.setUniformi("u_specular", 2);
            program.end();
            return program;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String id() {
        return "normal";
    }

    @Override
    public String displayName() {
        return "Normal Mapping";
    }
}
