package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.components.assets.FontComponent;
import net.lapidist.colony.components.base.PositionComponent;
import net.lapidist.colony.components.gui.GuiComponent;
import net.lapidist.colony.components.gui.LabelComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.gui.GuiInitEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.camera.CameraSystem;
import net.lapidist.colony.core.systems.delegate.EntityProcessPrincipal;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.gui.GuiAssetSystem;

import static com.artemis.E.E;

@Wire
public class GuiRenderSystem extends AbstractRenderSystem {

    private CameraSystem cameraSystem;
    private GuiAssetSystem assetSystem;
    private EntityFactorySystem entityFactorySystem;

    public GuiRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(GuiComponent.class), principal);
    }

    @Override
    protected void initialize() {
        Events.on(GuiInitEvent.class, guiInitEvent -> onInit());
        Events.on(ScreenResizeEvent.class, event -> onResize(event.getWidth(), event.getHeight()));
    }

    @Override
    protected void begin() {
        super.begin();
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
    }

    @Override
    protected void end() {
        super.end();
    }

    @Override
    protected void process(int e) {
        final PositionComponent posC = E(e).getPositionComponent();
        final LabelComponent labelC = E(e).getLabelComponent();
        final FontComponent fontC = E(e).getFontComponent();

        E(e).labelComponentText(Gdx.graphics.getFramesPerSecond() + " FPS");

        if (fontC != null) drawLabel(labelC, fontC, posC);
    }

    @Override
    protected void dispose() {
        super.dispose();
    }

    protected void onResize(int width, int height) {
        cameraSystem.guiCamera.setToOrtho(false, width, height);
        cameraSystem.guiCamera.update();
    }

    protected void onInit() {
        int e = entityFactorySystem.create(entityFactorySystem.getArchetype("label"));

        E(e).positionComponentPosition(new Vector3(16, 32, 0));
        E(e).fontComponentFont(assetSystem.getFont("default"));
        E(e).labelComponentText(Gdx.graphics.getFramesPerSecond() + " FPS");
        E(e).renderableComponentLayer(1000);
    }
}
