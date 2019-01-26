package net.lapidist.colony.core.events.map;

import net.lapidist.colony.core.events.AbstractEvent;

public class HoverTileOutsideReachEvent extends AbstractEvent {

    private int gridX;
    private int gridY;

    public HoverTileOutsideReachEvent(String payload) {
        super(payload);
    }

    public HoverTileOutsideReachEvent(int gridX, int gridY) {
        this("gridX=" + gridX +
                ", gridY=" + gridY +
                '}');

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
