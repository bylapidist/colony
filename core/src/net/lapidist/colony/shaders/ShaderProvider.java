package net.lapidist.colony.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;

public class ShaderProvider extends DefaultShaderProvider {

    @Override
    protected Shader createShader(Renderable renderable) {
//        return new SunShader(renderable);

        return super.createShader(renderable);
    }
}
