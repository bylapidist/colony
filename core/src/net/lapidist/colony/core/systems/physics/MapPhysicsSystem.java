package net.lapidist.colony.core.systems.physics;

import box2dLight.RayHandler;
import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.lapidist.colony.components.DynamicBodyComponent;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.abstracts.AbstractCameraSystem;

import static com.artemis.E.E;

@Wire
public class MapPhysicsSystem extends AbstractRenderSystem {

    private AbstractCameraSystem cameraSystem;
    private TimeSystem timeSystem;
    private World physicsWorld;
    private RayHandler rayHandler;
    private Vector2 tmpVec2;
    private Box2DDebugRenderer debugRenderer;

    public MapPhysicsSystem() {
        super(Aspect.all(DynamicBodyComponent.class));

        tmpVec2 = new Vector2();
        physicsWorld = new World(new Vector2(0, 0), false);
        debugRenderer = new Box2DDebugRenderer();
        rayHandler = new RayHandler(physicsWorld);
    }

    @Override
    protected void initialize() {
        rayHandler.setAmbientLight(timeSystem.getCurrentTime().getAmbientLight(timeSystem.getCurrentTime()));
        rayHandler.setBlurNum(3);
        rayHandler.setShadows(true);
    }

    @Override
    protected void process(Entity e) {
        physicsWorld.step(Gdx.graphics.getDeltaTime(), 6, 2);

        E(e).dynamicBodyComponentBody().setTransform(
                tmpVec2.set(
                        (E(e).worldPositionComponentPosition().x + (E(e).originComponentOrigin().x * E(e).textureComponentTexture().getWidth())) / Constants.PPM,
                        (E(e).worldPositionComponentPosition().y + (E(e).originComponentOrigin().y * E(e).textureComponentTexture().getHeight())) / Constants.PPM
                ),
                E(e).rotationComponentRotation() + MathUtils.PI
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
        debugRenderer.render(physicsWorld, cameraSystem.camera.combined.cpy().scl(Constants.PPM));
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

    public World getPhysicsWorld() {
        return physicsWorld;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }
}
