package net.lapidist.colony.core.events;

import net.lapidist.colony.common.events.IEvent;
import net.lapidist.colony.core.Colony;
import net.lapidist.colony.core.screens.MapScreen;

public class GameLoadEvent implements IEvent {
    public GameLoadEvent() {
        System.out.println("GameLoadEvent");
        Colony.getInstance().setScreen(new MapScreen());
    }
}
