package net.lapidist.colony.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import static net.lapidist.colony.Constants.*;

public class SunShader extends BaseShader {

    protected final int u_projTrans = register(new Uniform("u_projTrans"));
    protected final int u_worldTrans = register(new Uniform("u_worldTrans"));
    protected final int u_color = register(new Uniform("u_color"));

    private ShaderProgram shaderProgram;

    public SunShader(Renderable renderable) {
        super();

        String vertexShader = resourceLoader.getShader("sunVertexShader");
        String fragShader = resourceLoader.getShader("sunFragShader");

        shaderProgram = new ShaderProgram(vertexShader, fragShader);

        if (!shaderProgram.isCompiled()) throw new RuntimeException("Couldn't compile shader " + program.getLog());
    }

    @Override
    public void init() {
        super.init(shaderProgram, null);
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        shaderProgram.begin();
        context.setDepthTest(GL20.GL_LEQUAL, 0f, 1f);
        context.setDepthMask(true);
        set(u_projTrans, camera.combined);
    }

    @Override
    public void render(Renderable renderable) {
        set(u_worldTrans, renderable.worldTransform);

        ColorAttribute colorAttr = (ColorAttribute)renderable.material.get(ColorAttribute.Diffuse);
        set(u_color, colorAttr.color);

        renderable.meshPart.render(shaderProgram);
    }

    @Override
    public void end() {
        shaderProgram.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        shaderProgram.dispose();
    }
}
