package net.lapidist.colony.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ai.msg.MessageManager;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.client.screens.MapScreen;

public class Colony extends Game {

    @Override
    public void create() {
        MessageManager.getInstance().dispatchMessage(0, null, Events.GAME_INIT);
        setScreen(new MapScreen());
    }
}
