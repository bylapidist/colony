package net.lapidist.colony.core.systems.map;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.assets.TextureComponent;
import net.lapidist.colony.components.base.OriginComponent;
import net.lapidist.colony.components.base.WorldPositionComponent;
import net.lapidist.colony.components.base.RotationComponent;
import net.lapidist.colony.components.base.ScaleComponent;
import net.lapidist.colony.components.render.InvisibleComponent;
import net.lapidist.colony.components.base.SortableComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.logic.MapInitEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.abstracts.AbstractCameraSystem;

import static com.artemis.E.E;

@Wire
public class MapRenderSystem extends AbstractRenderSystem {

    private AbstractCameraSystem cameraSystem;
    private EntityFactorySystem entityFactorySystem;
    private MapGenerationSystem mapGenerationSystem;
    private MapPhysicsSystem mapPhysicsSystem;
    private final Vector2 tmpVec2 = new Vector2();

    public MapRenderSystem() {
        super(Aspect.all(SortableComponent.class).exclude(InvisibleComponent.class));
    }

    @Override
    protected void initialize() {
        super.initialize();

        Events.on(MapInitEvent.class, mapInitEvent -> onInit());
        Events.on(ScreenResizeEvent.class, event -> onResize(event.getWidth(), event.getHeight()));
    }

    @Override
    protected void begin() {
        batch.begin();
        batch.setProjectionMatrix(cameraSystem.camera.combined);
    }

    @Override
    protected void end() {
        batch.end();
    }

    @Override
    protected void process(Entity e) {
        if (
                E(e).hasTextureComponent() &&
                E(e).hasRotationComponent() &&
                E(e).hasOriginComponent() &&
                E(e).hasWorldPositionComponent() &&
                E(e).hasScaleComponent()
        ) {
            final WorldPositionComponent posC = E(e).getWorldPositionComponent();
            final TextureComponent textureC = E(e).getTextureComponent();
            final RotationComponent rotationC = E(e).getRotationComponent();
            final ScaleComponent scaleC = E(e).getScaleComponent();
            final OriginComponent originC = E(e).getOriginComponent();

            tmpVec2.set(cameraSystem.screenCoordsFromWorldCoords(posC.getPosition().x, posC.getPosition().y));
            if (isWithinBounds(tmpVec2.x, tmpVec2.y))
                drawTexture(textureC, rotationC, originC, posC, scaleC, cameraSystem.zoom);
        }
    }

    @Override
    protected void dispose() {
        super.dispose();
    }

    protected void onResize(int width, int height) {
        cameraSystem.camera.setToOrtho(false, width, height);
        cameraSystem.camera.update();
    }

    protected void onInit() {
        mapGenerationSystem.generate();
    }
}
