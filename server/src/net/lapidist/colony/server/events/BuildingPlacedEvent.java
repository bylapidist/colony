package net.lapidist.colony.server.events;

import net.lapidist.colony.core.events.AbstractDataEvent;

/**
 * Event fired when a building is placed on the map.
 */
public final class BuildingPlacedEvent extends AbstractDataEvent {
    private final int x;
    private final int y;
    private final String buildingType;

    public BuildingPlacedEvent(final int xToSet, final int yToSet, final String typeToSet) {
        this.x = xToSet;
        this.y = yToSet;
        this.buildingType = typeToSet;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getBuildingType() {
        return buildingType;
    }

    @Override
    public String toString() {
        return format("x", x, "y", y, "buildingType", buildingType);
    }
}
