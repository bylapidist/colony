package net.lapidist.colony.core.events.logic;

import net.lapidist.colony.core.Colony;
import net.lapidist.colony.core.events.AbstractEvent;
import net.lapidist.colony.core.screens.MapScreen;

public class GameLoadEvent extends AbstractEvent {
    public GameLoadEvent() {
        Colony.getInstance().setScreen(new MapScreen());
    }

    @Override
    public String toString() {
        return "GameLoadEvent{}";
    }
}
