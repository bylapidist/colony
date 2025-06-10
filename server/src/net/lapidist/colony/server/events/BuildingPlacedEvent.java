package net.lapidist.colony.server.events;

import net.mostlyoriginal.api.event.common.Event;

/**
 * Event fired when a building is placed on the map.
 *
 * @param x            tile x coordinate
 * @param y            tile y coordinate
 * @param buildingType the building type
 */
public record BuildingPlacedEvent(int x, int y, String buildingType) implements Event {

    public BuildingPlacedEvent {
        // explicit constructor for future validation
    }

    @Override
    public String toString() {
        return String.format(
                "%s(x=%d, y=%d, buildingType=%s)",
                getClass().getSimpleName(),
                x,
                y,
                buildingType
        );
    }
}
