package net.lapidist.colony.server.events;

import net.mostlyoriginal.api.event.common.Event;

/**
 * Event fired when a tile selection state changes.
 *
 * @param x        tile x coordinate
 * @param y        tile y coordinate
 * @param selected whether the tile is selected
 */
public record TileSelectionEvent(int x, int y, boolean selected) implements Event {

    public TileSelectionEvent {
        // explicit constructor for future validation
    }

    @Override
    public String toString() {
        return String.format(
                "%s(x=%d, y=%d, selected=%s)",
                getClass().getSimpleName(),
                x,
                y,
                selected
        );
    }
}
