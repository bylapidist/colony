package net.lapidist.colony.client.screens;

import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.network.Client;

public class MapScreen implements Screen {

    private final World world;
    private Client client;

    public MapScreen() {
        Events.enableDebug();

        try {
            client = new Client(
                    Constants.DEFAULT_HOSTNAME,
                    Constants.DEFAULT_PORT
            );
            client.setState(Client.ClientState.CONNECTED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new SuperMapper(),
                        new TagManager()
                )
                .with(WorldConfigurationBuilder.Priority.NORMAL,
                        new ClearScreenSystem(Color.BLACK)
                )
                .build();
        world = new World(config);
    }

    @Override
    public final void render(final float delta) {
        world.setDelta(delta);
        world.process();
        Events.update();
    }

    @Override
    public final void resize(final int width, final int height) {
        Events.dispatch(0, Events.EventType.RESIZE);
    }

    @Override
    public final void pause() {
        Events.dispatch(0,  Events.EventType.PAUSE);
    }

    @Override
    public final void resume() {
        Events.dispatch(0,  Events.EventType.RESUME);
    }

    @Override
    public final void hide() {
        Events.dispatch(0, Events.EventType.HIDE);
    }

    @Override
    public final void show() {
        Events.dispatch(0, Events.EventType.SHOW);
    }

    @Override
    public final void dispose() {
        client.setState(Client.ClientState.DISCONNECTED);
        world.dispose();
    }
}
