package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import net.lapidist.colony.components.gui.GuiComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.gui.GuiInitEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.systems.Mappers;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
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
    }

    @Override
    protected void end() {
    }

    @Override
    protected void process(int e) {
    }

    @Override
    protected void dispose() {
    }

    protected void onResize(int width, int height) {
        cameraSystem.guiCamera.setToOrtho(false, width, height);
        cameraSystem.guiCamera.update();
    }

    protected void onInit() {

    }
}
