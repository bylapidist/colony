package net.lapidist.colony.core.events.gui;

import net.lapidist.colony.core.events.AbstractEvent;

public class ClickTileOutsideReachEvent extends AbstractEvent {

    private int gridX;
    private int gridY;

    private ClickTileOutsideReachEvent(String payload) {
        super(payload);
    }

    public ClickTileOutsideReachEvent(int gridX, int gridY) {
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
