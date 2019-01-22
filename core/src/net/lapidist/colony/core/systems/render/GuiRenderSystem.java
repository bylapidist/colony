package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import net.lapidist.colony.components.assets.FontComponent;
import net.lapidist.colony.components.assets.TextureComponent;
import net.lapidist.colony.components.gui.GuiComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.gui.GuiInitEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.systems.Mappers;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Origin;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.basic.Scale;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;

@Wire
public class GuiRenderSystem extends AbstractRenderSystem {

    private CameraSystem cameraSystem;
    private Mappers mappers;

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
        final Pos posC = mappers.mPos.get(e);
        final Label labelC = mappers.mLabel.get(e);
        final Angle angleC = mappers.mAngle.getSafe(e, Angle.NONE);
        final float scale = mappers.mScale.getSafe(e, Scale.DEFAULT).scale;
        final Origin originC = mappers.mOrigin.getSafe(e, defaultOrigin);

        batch.setColor(mappers.mTint.getSafe(e, Tint.WHITE).color);

        if (labelC != null && posC != null) drawLabel(labelC, angleC, originC, posC, scale, cameraSystem.zoom);
    }

    @Override
    protected void dispose() {
        super.dispose();
    }

    protected void onResize(int width, int height) {
        cameraSystem.guiCamera.setToOrtho(true, width, height);
        cameraSystem.guiCamera.update();
    }

    protected void onInit() {

    }
}
