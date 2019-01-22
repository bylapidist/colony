package net.lapidist.colony.core.screens;

import com.artemis.*;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Screen;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.events.logic.WorldInitEvent;

public class MapScreen implements Screen {

    private World world;

    public MapScreen() {
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new SuperMapper(),
                        new TagManager()
                )
//                .with(WorldConfigurationBuilder.Priority.NORMAL,

//                )
                .build();

        world = new World(config);

        Events.fire(new WorldInitEvent());
    }

    @Override
    public void show() {
    }

    @Override
    public void render(final float delta) {
        world.setDelta(delta);
        world.process();
    }

    @Override
    public void resize(final int width, final int height) {
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
