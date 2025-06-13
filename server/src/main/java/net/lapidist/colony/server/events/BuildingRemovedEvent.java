package net.lapidist.colony.server.events;

import net.mostlyoriginal.api.event.common.Event;

/** Event fired when a building is removed from the map.
 *
 * @param x tile x coordinate
 * @param y tile y coordinate
 */
public record BuildingRemovedEvent(int x, int y) implements Event {
    public BuildingRemovedEvent {
        // explicit constructor for future validation
    }

    @Override
    public String toString() {
        return String.format("%s(x=%d, y=%d)", getClass().getSimpleName(), x, y);
    }
}
