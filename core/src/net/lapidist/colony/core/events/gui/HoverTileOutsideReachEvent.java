package net.lapidist.colony.core.events.gui;

import net.lapidist.colony.core.events.IEvent;

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

    @Override
    public String toString() {
        return "HoverTileOutsideReachEvent{" +
                "gridX=" + gridX +
                ", gridY=" + gridY +
                '}';
    }
}
