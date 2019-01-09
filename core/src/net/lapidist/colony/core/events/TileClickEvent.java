package net.lapidist.colony.core.events;

import net.lapidist.colony.common.events.IEvent;
import net.lapidist.colony.components.TileComponent;

public class TileClickEvent implements IEvent {

    public final TileComponent tile;

    public TileClickEvent(TileComponent tile) {
        this.tile = tile;
    }
}
