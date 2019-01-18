package net.lapidist.colony.core.events;

import net.lapidist.colony.common.events.IEvent;

public class HoverTileOutsideReachEvent implements IEvent {

    private int gridX;
    private int gridY;

    public HoverTileOutsideReachEvent(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }
}
