package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.components.render.GuiComponent;
import net.lapidist.colony.core.Colony;
import net.lapidist.colony.core.events.ScreenResizeEvent;
import net.lapidist.colony.core.systems.camera.CameraSystem;

import static com.artemis.E.*;

public class GuiRenderingSystem extends EntityProcessingSystem {

    private BitmapFont font;
    private CameraSystem cameraSystem;

    public GuiRenderingSystem() {
        super(Aspect.all(
                GuiComponent.class
        ));
    }

    @Override
    protected void initialize() {
        Events.on(ScreenResizeEvent.class, event -> resize(event.width, event.height));
        font = Colony.getResourceLoader().getFont("default");
    }

    @Override
    protected void process(Entity e) {
        if (E(e).hasSpriteComponent()) {
            E(e).spriteComponentSprite().draw(Colony.getSpriteBatch());
        }
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
    }

    @Override
    protected void end() {
        Colony.getSpriteBatch().end();
    }

    private void resize(int width, int height) {
        cameraSystem.guiCamera.setToOrtho(false, width, height);
        cameraSystem.guiCamera.update();
    }
}
