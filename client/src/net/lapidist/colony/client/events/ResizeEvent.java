package net.lapidist.colony.client.events;

import net.mostlyoriginal.api.event.common.Event;

public final class ResizeEvent implements Event {

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
}
