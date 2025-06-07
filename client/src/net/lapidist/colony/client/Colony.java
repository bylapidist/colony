package net.lapidist.colony.client;

import com.badlogic.gdx.Game;
import net.lapidist.colony.client.core.io.Paths;
import net.lapidist.colony.client.screens.MapScreen;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.client.events.Events;
import net.lapidist.colony.client.events.GameInitEvent;

import java.io.IOException;

public final class Colony extends Game {

    private GameClient client;

    @Override
    public void create() {
        // Do global initialisation
        try {
            Paths.createGameFoldersIfNotExists();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Dispatch events
        Events.dispatch(new GameInitEvent());

        MapState state;
        try {
            client = new GameClient();
            client.start();
            state = client.getMapState();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Finally, transition to the first screen
        setScreen(new MapScreen(state, client));
    }

    @Override
    public void dispose() {
        if (client != null) {
            client.stop();
        }
        Events.dispose();
    }
}
