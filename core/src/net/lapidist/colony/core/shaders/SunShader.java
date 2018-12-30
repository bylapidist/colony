package net.lapidist.colony.core.shaders;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

import static net.lapidist.colony.core.Constants.*;

public class SunShader implements Shader {

    private String vertexShader = resourceLoader.getShader("sunVertShader");
    private String fragmentShader = resourceLoader.getShader("sunFragShader");

    private ShaderProgram program;
    private RenderContext context;
    private Camera camera;

    int u_projTrans;
    int u_worldTrans;
    int u_color;

    @Override
    public void init() {
        program = new ShaderProgram(vertexShader, fragmentShader);

        if (!program.isCompiled())
            throw new RuntimeException(program.getLog());

        u_projTrans = program.getUniformLocation("u_projTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_color = program.getUniformLocation("u_color");
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return false;
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        this.camera = camera;
        this.context = context;

        program.begin();
        program.setUniformMatrix(u_projTrans, camera.combined);
        context.setDepthTest(GL20.GL_EQUAL, 0f, 1f);
        context.setCullFace(GL20.GL_BACK);
    }

    @Override
    public void render(Renderable renderable) {
        program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
        program.setUniformf(u_color, MathUtils.random(), MathUtils.random(), MathUtils.random());

        renderable.meshPart.render(program);
    }

    @Override
    public void end() {
        program.end();
    }

    @Override
    public void dispose() {
        program.dispose();
    }
}
