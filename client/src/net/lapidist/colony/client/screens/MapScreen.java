package net.lapidist.colony.client.screens;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.client.systems.*;
import net.lapidist.colony.client.core.events.EventType;
import net.lapidist.colony.client.core.events.Events;
import net.lapidist.colony.client.core.events.payloads.ResizePayload;

public class MapScreen implements Screen {

    private final World world;

    public MapScreen() {
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new TagManager()
                )
                .with(WorldConfigurationBuilder.Priority.NORMAL,
                        new ClearScreenSystem(Color.BLACK),
                        new MapGenerationSystem(Constants.MAP_WIDTH, Constants.MAP_HEIGHT),
                        new PlayerCameraSystem(),
                        new InputSystem(),
                        new MapRenderSystem(),
                        new UISystem()
                )
                .build();
        world = new World(config);
    }

    @Override
    public final void render(final float deltaTime) {
        world.setDelta(deltaTime);
        world.process();
        Events.update();
    }

    @Override
    public final void resize(final int width, final int height) {
        Events.dispatch(EventType.RESIZE, new ResizePayload(width, height));
    }

    @Override
    public final void pause() {
        Events.dispatch(EventType.PAUSE);
    }

    @Override
    public final void resume() {
        Events.dispatch(EventType.RESUME);
    }

    @Override
    public final void hide() {
        Events.dispatch(EventType.HIDE);
    }

    @Override
    public final void show() {
        Events.dispatch(EventType.SHOW);
    }

    @Override
    public final void dispose() {
        Events.dispatch(EventType.DISPOSE);
    }
}
