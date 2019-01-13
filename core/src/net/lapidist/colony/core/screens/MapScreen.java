package net.lapidist.colony.core.screens;

import com.artemis.*;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Screen;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.events.WorldInitEvent;
import net.lapidist.colony.core.systems.camera.CameraSystem;
import net.lapidist.colony.core.systems.camera.PlayerCameraSystem;
import net.lapidist.colony.core.systems.logic.MapGenerationSystem;
import net.lapidist.colony.core.systems.logic.PlayerControlSystem;
import net.lapidist.colony.core.systems.render.ClearScreenSystem;
import net.lapidist.colony.core.systems.render.MapRenderingSystem;

public class MapScreen implements Screen {

    private World world;
    private MapRenderingSystem renderingSystem;

    public MapScreen() {
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(
                        new SuperMapper(),
                        new TagManager()
                )
                .with(
                        new ClearScreenSystem(),
                        new CameraSystem(1f),
                        new PlayerCameraSystem(),
                        new PlayerControlSystem(),
                        new MapGenerationSystem(128, 128, Constants.PPM),
                        new MapRenderingSystem()
                )
                .build();

        world = new World(config);
        renderingSystem = world.getSystem(MapRenderingSystem.class);

        Events.fire(new WorldInitEvent());
    }

    @Override
    public void show() {
        renderingSystem.show();
    }

    @Override
    public void render(float delta) {
        world.setDelta(delta);
        world.process();
        renderingSystem.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderingSystem.resize(width, height);
    }

    @Override
    public void pause() {
        renderingSystem.pause();
    }

    @Override
    public void resume() {
        renderingSystem.resume();
    }

    @Override
    public void hide() {
        renderingSystem.hide();
    }

    @Override
    public void dispose() {
        renderingSystem.dispose();
        world.dispose();
    }
}
