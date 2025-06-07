package net.lapidist.colony.server.events;

import net.mostlyoriginal.api.event.common.Event;

public final class TileSelectionEvent implements Event {
    private final int x;
    private final int y;
    private final boolean selected;

    public TileSelectionEvent(final int xToSet, final int yToSet, final boolean selectedToSet) {
        this.x = xToSet;
        this.y = yToSet;
        this.selected = selectedToSet;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isSelected() {
        return selected;
    }
}
