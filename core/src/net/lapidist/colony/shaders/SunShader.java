package net.lapidist.colony.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.core.core.Graphics;

import static net.lapidist.colony.core.Constants.*;

public class SunShader implements Shader {

    private String vertexShader = resourceLoader.getShader("sunVertShader");
    private String fragmentShader = resourceLoader.getShader("sunFragShader");
    private Texture noiseTexture = resourceLoader.getRegion("noise").getTexture();

    private ShaderProgram program;
    private RenderContext context;
    private Camera camera;

    private int u_projTrans;
    private int u_worldTrans;
    private int u_color;
    private int u_texture0;
    private int u_texture1;
    private int u_time;
    private int u_resolution;

    @Override
    public void init() {
        program = new ShaderProgram(vertexShader, fragmentShader);

        if (!program.isCompiled())
            throw new RuntimeException(program.getLog());

        u_projTrans = program.getUniformLocation("u_projTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_color = program.getUniformLocation("u_color");
        u_texture0 = program.getUniformLocation("u_texture0");
        u_texture1 = program.getUniformLocation("u_texture1");
        u_time = program.getUniformLocation("u_time");
        u_resolution = program.getUniformLocation("u_resolution");
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
        program.setUniformi(u_texture0, context.textureBinder.bind(noiseTexture));
        program.setUniformi(u_texture1, context.textureBinder.bind(noiseTexture));
        program.setUniformf(u_time, 5f);
        program.setUniformf(u_resolution, new Vector3(Graphics.width(), Graphics.width(), 0));

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
