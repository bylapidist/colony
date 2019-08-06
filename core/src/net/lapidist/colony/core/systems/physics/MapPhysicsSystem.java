package net.lapidist.colony.core.systems.physics;

import box2dLight.RayHandler;
import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import net.lapidist.colony.components.base.DynamicBodyComponent;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.logic.MapPhysicsInitEvent;
import net.lapidist.colony.core.events.map.ClickTileWithinReachEvent;
import net.lapidist.colony.core.systems.AbstractRenderSystem;
import net.lapidist.colony.core.systems.AbstractCameraSystem;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.logic.TimeSystem;
import net.lapidist.colony.core.systems.assets.MapAssetSystem;
import net.lapidist.colony.core.systems.generators.MapGeneratorSystem;

import static com.artemis.E.E;

@Wire
public class MapPhysicsSystem extends AbstractRenderSystem {

    private AbstractCameraSystem cameraSystem;
    private MapGeneratorSystem mapGeneratorSystem;
    private EntityFactorySystem entityFactorySystem;
    private TimeSystem timeSystem;
    private MapAssetSystem assetSystem;
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

        Events.fire(new MapPhysicsInitEvent());

        Events.on(ClickTileWithinReachEvent.class, event -> {
            int e = entityFactorySystem.create(entityFactorySystem.getArchetype("building"));

            E(e).textureComponentTexture(assetSystem.getTexture("grass"));
            E(e).rotationComponentRotation(0);
            E(e).originComponentOrigin(new Vector2(0.5f, 0.5f));
            E(e).worldPositionComponentPosition(new Vector3(
                    event.getGridX() * Constants.PPM,
                    event.getGridY() * Constants.PPM,
                    0
            ));
            E(e).scaleComponentScale(1);
            E(e).velocityComponentVelocity(new Vector2(0, 0));
            E(e).sortableComponentLayer(1);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                    0.5f,
                    0.5f,
                    new Vector2(0, 0),
                    0
            );

            E(e).dynamicBodyComponentFixtureDef().shape = shape;
            E(e).dynamicBodyComponentBodyDef().position.set(
                    E(e).worldPositionComponentPosition().x,
                    E(e).worldPositionComponentPosition().y
            );
            E(e).dynamicBodyComponentBodyDef().type = BodyDef.BodyType.StaticBody;
            E(e).dynamicBodyComponentBody(
                    physicsWorld.createBody(
                            E(e).dynamicBodyComponentBodyDef()
                    )
            );
            E(e).dynamicBodyComponentBody().createFixture(
                    E(e).dynamicBodyComponentFixtureDef()
            );
        });
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
