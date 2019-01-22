package net.lapidist.colony.core.events.gui;

import net.lapidist.colony.core.events.IEvent;

public class HoverTileWithinReachEvent implements IEvent {

    private int gridX;
    private int gridY;
    private float screenX;
    private float screenY;

    public HoverTileWithinReachEvent(int gridX, int gridY, float screenX, float screenY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public float getScreenX() {
        return screenX;
    }

    public void setScreenX(float screenX) {
        this.screenX = screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    public void setScreenY(float screenY) {
        this.screenY = screenY;
    }
}
