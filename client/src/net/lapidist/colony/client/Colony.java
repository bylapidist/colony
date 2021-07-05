package net.lapidist.colony.client;

import com.badlogic.gdx.Game;
import net.lapidist.colony.client.screens.MapScreen;
import net.lapidist.colony.client.core.events.EventType;
import net.lapidist.colony.client.core.events.Events;

public final class Colony extends Game {

    @Override
    public void create() {
        Events.dispatch(EventType.GAME_INIT);
        setScreen(new MapScreen());
    }

    @Override
    public void dispose() {
        Events.dispose();
    }
}
