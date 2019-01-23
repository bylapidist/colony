package net.lapidist.colony.core.systems.map;

import box2dLight.RayHandler;
import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import net.lapidist.colony.components.base.DynamicBodyComponent;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.camera.CameraSystem;
import net.lapidist.colony.core.systems.delegate.EntityProcessPrincipal;

@Wire
public class MapPhysicsSystem extends AbstractRenderSystem {

    private CameraSystem cameraSystem;
    private World physicsWorld;
    private RayHandler rayHandler;
    private Vector2 tmpVec2;
    private Box2DDebugRenderer debugRenderer;

    public MapPhysicsSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(DynamicBodyComponent.class), principal);

        tmpVec2 = new Vector2();
        physicsWorld = new World(new Vector2(0, 0), false);
        debugRenderer = new Box2DDebugRenderer();
        rayHandler = new RayHandler(physicsWorld);
    }

    @Override
    protected void initialize() {
        rayHandler.setAmbientLight(0.1f, 0.1f, 0.1f, 0.1f);
        rayHandler.setBlurNum(3);
        rayHandler.setShadows(true);
    }

    @Override
    protected void process(int e) {
        physicsWorld.step(Gdx.graphics.getDeltaTime(), 6, 2);
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
        super.dispose();
        rayHandler.dispose();
        physicsWorld.dispose();
        debugRenderer.dispose();
    }

    @Override
    protected void onResize(int width, int height) {

    }

    @Override
    protected void onInit() {
    }
}
