package net.lapidist.colony.client;

import com.badlogic.gdx.Game;
import net.lapidist.colony.client.core.utils.PathUtils;
import net.lapidist.colony.client.screens.MapScreen;
import net.lapidist.colony.client.core.events.EventType;
import net.lapidist.colony.client.core.events.Events;

import java.io.IOException;

public final class Colony extends Game {

    @Override
    public void create() {
        // Do global initialisation
        try {
            PathUtils.createGameFoldersIfNotExists();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Dispatch events
        Events.dispatch(EventType.GAME_INIT);

        // Finally, transition to the first screen
        setScreen(new MapScreen());
    }

    @Override
    public void dispose() {
        Events.dispose();
    }
}
