package net.lapidist.colony.core.events.render;

import net.lapidist.colony.core.events.AbstractEvent;

public class ScreenResizeEvent extends AbstractEvent {

    private int width;
    private int height;

    public ScreenResizeEvent(int width, int height) {
        super(
                "width=" + width +
                ", height=" + height
        );

        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
