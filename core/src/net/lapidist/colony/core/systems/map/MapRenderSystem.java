package net.lapidist.colony.core.systems.map;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import net.lapidist.colony.components.assets.TextureComponent;
import net.lapidist.colony.components.base.OriginComponent;
import net.lapidist.colony.components.base.PositionComponent;
import net.lapidist.colony.components.base.RotationComponent;
import net.lapidist.colony.components.base.ScaleComponent;
import net.lapidist.colony.components.render.InvisibleComponent;
import net.lapidist.colony.components.base.SortableComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.logic.MapInitEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.camera.CameraSystem;
import net.lapidist.colony.core.systems.delegate.EntityProcessPrincipal;

import static com.artemis.E.E;

@Wire
public class MapRenderSystem extends AbstractRenderSystem {

    private CameraSystem cameraSystem;
    private EntityFactorySystem entityFactorySystem;
    private MapGenerationSystem mapGenerationSystem;

    public MapRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(SortableComponent.class).exclude(InvisibleComponent.class), principal);
    }

    @Override
    protected void initialize() {
        super.initialize();

        Events.on(MapInitEvent.class, mapInitEvent -> onInit());
        Events.on(ScreenResizeEvent.class, event -> onResize(event.getWidth(), event.getHeight()));
    }

    @Override
    protected void begin() {
        super.begin();
        batch.setProjectionMatrix(cameraSystem.camera.combined);
    }

    @Override
    protected void end() {
        super.end();
    }

    @Override
    protected void process(final int e) {
        if (
                E(e).hasTextureComponent() &&
                E(e).hasRotationComponent() &&
                E(e).hasOriginComponent() &&
                E(e).hasPositionComponent() &&
                E(e).hasScaleComponent()
        ) {
            final PositionComponent posC = E(e).getPositionComponent();
            final TextureComponent textureC = E(e).getTextureComponent();
            final RotationComponent rotationC = E(e).getRotationComponent();
            final ScaleComponent scaleC = E(e).getScaleComponent();
            final OriginComponent originC = E(e).getOriginComponent();

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
