package net.lapidist.colony.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ai.msg.MessageManager;
import net.lapidist.colony.core.systems.Events;
import net.lapidist.colony.core.screens.MapScreen;

public class Colony extends Game {

    @Override
    public void create() {
        MessageManager.getInstance().dispatchMessage(0, null, Events.GAME_INIT);
        setScreen(new MapScreen());
    }
}
