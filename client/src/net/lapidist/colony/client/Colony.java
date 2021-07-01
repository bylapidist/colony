package net.lapidist.colony.client;

import com.badlogic.gdx.Game;
import net.lapidist.colony.client.screens.MapScreen;
import net.lapidist.colony.core.events.Events;

public final class Colony extends Game {


    @Override
    public void create() {
        Events.dispatch(0, Events.EventType.GAME_INIT);
        Events.enableDebug();
        setScreen(new MapScreen());
    }

    @Override
    public void dispose() {
        Events.dispose();
    }
}
