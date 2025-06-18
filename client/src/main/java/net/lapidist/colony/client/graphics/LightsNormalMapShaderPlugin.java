package net.lapidist.colony.client.graphics;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.client.core.io.FileLocation;

/**
 * Shader plugin that enables Box2DLights along with normal mapping.
 */
public final class LightsNormalMapShaderPlugin implements LightingPlugin, UniformUpdater {

    private RayHandler rayHandler;
    private World world;
    private LightingSystem lightingSystem;
    private final com.badlogic.gdx.math.Vector3 lightDir = new com.badlogic.gdx.math.Vector3(0f, 0f, 1f);
    private final com.badlogic.gdx.math.Vector3 viewDir = new com.badlogic.gdx.math.Vector3(0f, 0f, 1f);

    /** Assign the system providing the sun direction. */
    public void setLightingSystem(final LightingSystem system) {
        this.lightingSystem = system;
    }

    @Override
    public ShaderProgram create(final ShaderManager manager) {
        if (Gdx.gl20 == null) {
            return null;
        }
        try {
            world = new World(new Vector2(), false);
            rayHandler = new RayHandler(world);
            rayHandler.setAmbientLight(1f, 1f, 1f, 1f);
            return manager.load(FileLocation.INTERNAL,
                    "shaders/normal.vert", "shaders/normal.frag");
        } catch (Exception ex) {
            if (world != null) {
                world.dispose();
                world = null;
            }
            rayHandler = null;
            return null;
        }
    }

    @Override
    public RayHandler getRayHandler() {
        return rayHandler;
    }

    /** Current Box2D world used for occlusion bodies. */
    public World getWorld() {
        return world;
    }

    @Override
    public String id() {
        return "lights-normalmap";
    }

    @Override
    public String displayName() {
        return "Lighting Normal Map";
    }

    @Override
    public void dispose() {
        if (world != null) {
            world.dispose();
            world = null;
        }
        if (rayHandler != null) {
            rayHandler.dispose();
            rayHandler = null;
        }
    }

    @Override
    public void applyUniforms(final ShaderProgram program) {
        if (lightingSystem != null) {
            program.setUniformf("u_lightDir", lightingSystem.getSunDirection(lightDir));
        } else {
            program.setUniformf("u_lightDir", lightDir);
        }
        viewDir.set(0f, 0f, 1f);
        program.setUniformf("u_viewDir", viewDir);
    }
}
