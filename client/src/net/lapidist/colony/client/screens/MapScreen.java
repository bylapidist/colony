package net.lapidist.colony.client.screens;

import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.network.Client;

public class MapScreen implements Screen {

    private final World world;

    public MapScreen() {
        MessageManager.getInstance().setDebugEnabled(true);

        try {
            Client client = new Client(
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
        MessageManager.getInstance().update();
    }

    @Override
    public final void resize(final int width, final int height) {
        MessageManager.getInstance().dispatchMessage(0, null, Events.RESIZE);
    }

    @Override
    public final void pause() {
        MessageManager.getInstance().dispatchMessage(0, null, Events.PAUSE);
    }

    @Override
    public final void resume() {
        MessageManager.getInstance().dispatchMessage(0, null, Events.RESUME);
    }

    @Override
    public final void hide() {
        MessageManager.getInstance().dispatchMessage(0, null, Events.HIDE);
    }

    @Override
    public final void show() {
        MessageManager.getInstance().dispatchMessage(0, null, Events.SHOW);
    }

    @Override
    public final void dispose() {
        world.dispose();
        MessageManager.getInstance().clear();
    }
}
