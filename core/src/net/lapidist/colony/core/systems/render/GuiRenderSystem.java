package net.lapidist.colony.core.systems.render;

import com.artemis.ArchetypeBuilder;
import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.components.assets.FontComponent;
import net.lapidist.colony.components.base.PositionComponent;
import net.lapidist.colony.components.gui.GuiComponent;
import net.lapidist.colony.components.gui.LabelComponent;
import net.lapidist.colony.components.render.RenderableComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.gui.GuiInitEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.gui.GuiAssetSystem;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;

import static com.artemis.E.E;

@Wire
public class GuiRenderSystem extends AbstractRenderSystem {

    private CameraSystem cameraSystem;
    private GuiAssetSystem assetSystem;

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
        
        labelC.setText(Gdx.graphics.getFramesPerSecond() + " FPS");

        if (fontC != null && posC != null) drawLabel(labelC, fontC, posC);
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
        Entity e = world.createEntity(new ArchetypeBuilder()
                .add(PositionComponent.class)
                .add(FontComponent.class)
                .add(LabelComponent.class)
                .add(GuiComponent.class)
                .add(RenderableComponent.class)
                .build(world));

        E(e).getPositionComponent().setPosition(new Vector3(16, 32, 0));
        E(e).getFontComponent().setFont(assetSystem.getFont("default"));
        E(e).getLabelComponent().setText(Gdx.graphics.getFramesPerSecond() + " FPS");
    }
}
