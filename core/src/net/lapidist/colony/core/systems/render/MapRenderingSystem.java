package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.common.postprocessing.effects.Fxaa;
import net.lapidist.colony.common.postprocessing.effects.MotionBlur;
import net.lapidist.colony.components.render.RenderableComponent;
import net.lapidist.colony.components.render.SpriteComponent;
import net.lapidist.colony.components.render.UpdatableComponent;
import net.lapidist.colony.core.Colony;
import net.lapidist.colony.core.events.ScreenResizeEvent;
import net.lapidist.colony.core.systems.camera.CameraSystem;
import net.lapidist.colony.core.systems.logic.MapGenerationSystem;

import static com.artemis.E.*;

@Wire
public class MapRenderingSystem extends EntityProcessingSystem {

    private CameraSystem cameraSystem;
    private MapGenerationSystem mapGenerationSystem;

    public MapRenderingSystem() {
        super(Aspect.all(
                RenderableComponent.class,
                UpdatableComponent.class,
                SpriteComponent.class
        ));
    }

    @Override
    protected void initialize() {
        Events.on(ScreenResizeEvent.class, event -> resize(event.width, event.height));

        MotionBlur motionBlur = new MotionBlur();
        Fxaa fxaa = new Fxaa(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        motionBlur.setBlurOpacity(0.2f);

        Colony.getPostProcessor().addEffect(motionBlur);
        Colony.getPostProcessor().addEffect(fxaa);
    }

    @Override
    protected void process(Entity e) {
        E(e).spriteComponentSprite().draw(Colony.getSpriteBatch());
    }

    @Override
    protected void begin() {
        cameraSystem.camera.update();
        Colony.getPostProcessor().capture();
        Colony.getSpriteBatch().setProjectionMatrix(cameraSystem.camera.combined);
        Colony.getSpriteBatch().begin();
    }

    @Override
    protected void end() {
        Colony.getSpriteBatch().end();
        Colony.getPostProcessor().render();
    }

    private void resize(int width, int height) {
        cameraSystem.camera.setToOrtho(true, width, height);
        cameraSystem.camera.update();
    }
}
