package net.lapidist.colony.core.systems.render;

import com.artemis.ArchetypeBuilder;
import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.components.assets.FontComponent;
import net.lapidist.colony.components.gui.GuiComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.gui.GuiInitEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.systems.Mappers;
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.gui.GuiAssetSystem;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Render;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;

@Wire
public class GuiRenderSystem extends AbstractRenderSystem {

    private CameraSystem cameraSystem;
    private GuiAssetSystem assetSystem;
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
        final FontComponent fontC = mappers.mFont.get(e);

        batch.setColor(mappers.mTint.getSafe(e, Tint.WHITE).color);
        mappers.mLabel.get(e).text = Gdx.graphics.getFramesPerSecond() + " FPS";

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
                .add(Pos.class)
                .add(FontComponent.class)
                .add(Label.class)
                .add(GuiComponent.class)
                .add(Render.class)
                .build(world));

        mappers.mPos.create(e);
        mappers.mFont.create(e);
        mappers.mLabel.create(e);

        mappers.mPos.get(e).set(new Vector3(16, 32, 0));
        mappers.mFont.get(e).setFont(assetSystem.getFont("default"));
        mappers.mLabel.get(e).set(new Label(Gdx.graphics.getFramesPerSecond() + " FPS"));
    }
}
