package net.lapidist.colony.core.systems.render;

import box2dLight.RayHandler;
import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.lapidist.colony.components.DynamicBodyComponent;
import net.lapidist.colony.components.PointLightComponent;
import net.lapidist.colony.components.render.SpriteComponent;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.systems.camera.CameraSystem;
import net.lapidist.colony.core.systems.logic.MapGenerationSystem;

import static com.artemis.E.*;

@Wire
public class LightRenderingSystem extends EntityProcessingSystem {

    private CameraSystem cameraSystem;
    private MapGenerationSystem mapGenerationSystem;
    private World physicsWorld;
    private RayHandler rayHandler;
    private Vector2 tmpVec2;

    public LightRenderingSystem() {
        super(Aspect.all(
                PointLightComponent.class,
                DynamicBodyComponent.class,
                SpriteComponent.class
        ));
    }

    @Override
    protected void initialize() {
        tmpVec2 = new Vector2();
        physicsWorld = new World(new Vector2(0, 0), true);
        rayHandler = new RayHandler(physicsWorld);
        rayHandler.setAmbientLight(0.1f, 0.1f, 0.1f, 0.01f);
        rayHandler.setBlurNum(3);
        rayHandler.setShadows(true);
    }

    @Override
    protected void process(Entity e) {
        physicsWorld.step(Gdx.graphics.getDeltaTime(), 6, 2);

        E(e).dynamicBodyComponentBody().setTransform(
                tmpVec2.set(
                        E(e).spriteComponentSprite().getOriginX() / mapGenerationSystem.getTileWidth(),
                        E(e).spriteComponentSprite().getOriginY() / mapGenerationSystem.getTileHeight()
                ),
                0
        );
    }

    @Override
    protected void begin() {
        rayHandler.update();
        rayHandler.setCombinedMatrix(
                cameraSystem.camera.combined.cpy().scl(Constants.PPM),
                0,
                0,
                cameraSystem.camera.viewportWidth * cameraSystem.camera.zoom,
                cameraSystem.camera.viewportHeight * cameraSystem.camera.zoom
        );
    }

    @Override
    protected void end() {
        rayHandler.render();
    }

    @Override
    protected void dispose() {
        rayHandler.dispose();
        physicsWorld.dispose();
    }

    public World getPhysicsWorld() {
        return physicsWorld;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }
}
