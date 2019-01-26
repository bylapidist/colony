package net.lapidist.colony.core;

import com.badlogic.gdx.Game;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.logic.GameInitEvent;
import net.lapidist.colony.core.screens.MapScreen;

public class Colony extends Game {

    private static Colony instance;

    @Override
    public void create() {
        instance = this;
        restart();
    }

    public void restart() {
        Events.fire(new GameInitEvent());
        setScreen(new MapScreen());
    }

    public static Colony getInstance() {
        return instance;
    }
}
