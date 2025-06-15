package net.lapidist.colony.client.graphics;

import box2dLight.RayHandler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Shader plugin that adds Box2DLights rendering support.
 */
public final class Box2dLightsPlugin implements ShaderPlugin {

    private RayHandler rayHandler;

    @Override
    public ShaderProgram create(final ShaderManager manager) {
        World world = new World(new Vector2(), false);
        rayHandler = new RayHandler(world);
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
}
