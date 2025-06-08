package net.lapidist.colony.server.events;

import net.lapidist.colony.core.events.AbstractDataEvent;

public final class TileSelectionEvent extends AbstractDataEvent {
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

    @Override
    public String toString() {
        return format("x", x, "y", y, "selected", selected);
    }
}
