package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.components.render.RenderableComponent;
import net.lapidist.colony.core.Colony;
import net.lapidist.colony.core.events.ScreenResizeEvent;
import net.lapidist.colony.core.systems.camera.CameraSystem;

public class GuiRenderSystem extends EntityProcessingSystem {

    private Array<Entity> renderQueue;
    private BitmapFont font;
    private CameraSystem cameraSystem;

    public GuiRenderSystem() {
        super(Aspect.all(RenderableComponent.class));

        this.renderQueue = new Array<>();
        font = Colony.getResourceLoader().getFont("default");
    }

    @Override
    protected void process(Entity entity) {
        if (!renderQueue.contains(entity, true)) {
            renderQueue.add(entity);
        }
    }

    @Override
    protected void initialize() {
        Events.on(ScreenResizeEvent.class, event -> {
            resize(event.width, event.height);
        });
    }

    private void resize(int width, int height) {
        cameraSystem.guiCamera.setToOrtho(false, width, height);
        cameraSystem.guiCamera.update();
    }

    @Override
    protected void begin() {
        Colony.getSpriteBatch().setProjectionMatrix(cameraSystem.guiCamera.combined);
        Colony.getSpriteBatch().begin();

        font.draw(
                Colony.getSpriteBatch(),
                Gdx.graphics.getFramesPerSecond() + " FPS",
                32,
                Gdx.graphics.getHeight() - 32
        );

        Colony.getSpriteBatch().end();
    }

    @Override
    protected void dispose() {
        renderQueue.forEach(Entity::deleteFromWorld);
        renderQueue.clear();
    }
}
