package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.components.assets.TextureComponent;
import net.lapidist.colony.components.base.OriginComponent;
import net.lapidist.colony.components.base.PositionComponent;
import net.lapidist.colony.components.base.RotationComponent;
import net.lapidist.colony.components.base.ScaleComponent;
import net.lapidist.colony.components.render.InvisibleComponent;
import net.lapidist.colony.components.render.RenderableComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.logic.MapInitEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.camera.CameraSystem;
import net.lapidist.colony.core.systems.delegate.EntityProcessPrincipal;
import net.lapidist.colony.core.systems.map.MapAssetSystem;

import static com.artemis.E.E;

@Wire
public class MapRenderSystem extends AbstractRenderSystem {

    private CameraSystem cameraSystem;
    private MapAssetSystem assetSystem;
    private EntityFactorySystem entityFactorySystem;

    public MapRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(RenderableComponent.class).exclude(InvisibleComponent.class), principal);
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
        int e = entityFactorySystem.create(entityFactorySystem.getArchetype("terrain"));

        E(e).textureComponentTexture(assetSystem.getTexture("dirt"));
        E(e).rotationComponentRotation(0);
        E(e).originComponentOrigin(new Vector2(0.5f, 0.5f));
        E(e).positionComponentPosition(new Vector3(0, 0, 0));
        E(e).scaleComponentScale(1);
        E(e).renderableComponentLayer(0);
    }
}
