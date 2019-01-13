package net.lapidist.colony.core.events;

import net.lapidist.colony.common.events.IEvent;

public class WorldInitEvent implements IEvent {
    public WorldInitEvent() {
        System.out.println("WorldInitEvent");
    }
}
