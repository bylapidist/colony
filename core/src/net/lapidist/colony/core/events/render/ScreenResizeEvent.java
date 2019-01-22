package net.lapidist.colony.core.events.render;

import net.lapidist.colony.core.events.AbstractEvent;

public class ScreenResizeEvent extends AbstractEvent {

    private int width;
    private int height;

    public ScreenResizeEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "ScreenResizeEvent{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
