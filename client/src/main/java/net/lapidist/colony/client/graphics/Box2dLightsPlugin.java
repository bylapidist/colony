package net.lapidist.colony.client.graphics;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;


/**
 * Shader plugin that adds Box2DLights rendering support.
 */
public final class Box2dLightsPlugin implements LightingPlugin {

    private RayHandler rayHandler;
    private World world;

    @Override
    public ShaderProgram create(final ShaderManager manager) {
        if (Gdx.gl20 == null) {
            return null;
        }
        try {
            world = new World(new Vector2(), false);
            rayHandler = new RayHandler(world);
            rayHandler.setAmbientLight(1f, 1f, 1f, 1f);
        } catch (Exception ex) {
            rayHandler = null;
        }
        return null;
    }

    /**
     * Access the created {@link RayHandler} instance.
     *
     * @return the handler or {@code null} if {@link #create(ShaderManager)} has not been called
     */
    public RayHandler getRayHandler() {
        return rayHandler;
    }

    @Override
    public String id() {
        return "box2dlights";
    }

    @Override
    public String displayName() {
        return "Box2DLights";
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
}
