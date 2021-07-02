package net.lapidist.colony.client;

import com.badlogic.gdx.Game;
import net.lapidist.colony.client.screens.MapScreen;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.events.EventType;
import net.lapidist.colony.core.events.Events;

public final class Colony extends Game {

    @Override
    public void create() {
        Events.dispatch(EventType.GAME_INIT);
        Events.setDebugEnabled(Constants.DEBUG);
        setScreen(new MapScreen());
    }

    @Override
    public void dispose() {
        Events.dispose();
    }
}
