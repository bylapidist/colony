package net.lapidist.colony.core.events;

import net.lapidist.colony.common.events.IEvent;

public class ScreenResizeEvent implements IEvent {

    public int width;
    public int height;

    public ScreenResizeEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
