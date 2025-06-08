package net.lapidist.colony.client.events;

import net.lapidist.colony.core.events.AbstractDataEvent;

public final class ResizeEvent extends AbstractDataEvent {

    private final int width;
    private final int height;

    public ResizeEvent(final int widthToSet, final int heightToSet) {
        this.width = widthToSet;
        this.height = heightToSet;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return format("width", width, "height", height);
    }
}
