package net.lapidist.colony.client;

import com.badlogic.gdx.Game;
import net.lapidist.colony.client.screens.MapScreen;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.network.Client;

public final class Colony extends Game {

    private Client client;

    @Override
    public void create() {
        Events.enableDebug();

        try {
            client = new Client(
                    Constants.DEFAULT_HOSTNAME,
                    Constants.DEFAULT_PORT
            );

            client.setState(Client.ClientState.CONNECTED);

            Events.dispatch(0, Events.EventType.GAME_INIT);

            System.out.printf(
                    "[Client] Connected to server: %s:%s\n",
                    Constants.DEFAULT_HOSTNAME,
                    Constants.DEFAULT_PORT
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        setScreen(new MapScreen());
    }

    @Override
    public void dispose() {
        client.setState(Client.ClientState.DISCONNECTED);
        Events.dispose();
    }
}
