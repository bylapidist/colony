package net.lapidist.colony.core.screens;

import com.artemis.*;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Screen;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.events.ScreenResizeEvent;
import net.lapidist.colony.core.events.WorldInitEvent;
import net.lapidist.colony.core.systems.camera.CameraSystem;
import net.lapidist.colony.core.systems.camera.PlayerCameraSystem;
import net.lapidist.colony.core.systems.logic.EntityFactorySystem;
import net.lapidist.colony.core.systems.logic.PlayerControlSystem;
import net.lapidist.colony.core.systems.render.MapGenerationSystem;
import net.lapidist.colony.core.systems.render.GuiRenderSystem;
import net.lapidist.colony.core.systems.render.MapRenderingSystem;

public class MapScreen implements Screen {

    private World world;

    public MapScreen() {
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new SuperMapper(),
                        new TagManager()
                )
                .with(WorldConfigurationBuilder.Priority.NORMAL,
                        new CameraSystem(1f),
                        new PlayerCameraSystem(),
                        new PlayerControlSystem(),
                        new EntityFactorySystem(),
                        new MapGenerationSystem(128, 128, Constants.PPM, Constants.PPM),
                        new MapRenderingSystem(),
                        new GuiRenderSystem()
                )
                .build();

        world = new World(config);

        Events.fire(new WorldInitEvent());
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        world.setDelta(delta);
        world.process();
    }

    @Override
    public void resize(int width, int height) {
        Events.fire(new ScreenResizeEvent(width, height));
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
