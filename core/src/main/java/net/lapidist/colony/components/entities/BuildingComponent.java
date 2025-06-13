package net.lapidist.colony.components.entities;

import net.lapidist.colony.components.AbstractBoundedComponent;

/** Component storing the id of a building type. */
public final class BuildingComponent extends AbstractBoundedComponent {
    private String buildingType;
    private boolean dirty;

    public String getBuildingType() {
        return buildingType;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(final boolean dirtyToSet) {
        this.dirty = dirtyToSet;
    }

    public void setBuildingType(final String buildingTypeToSet) {
        this.buildingType = buildingTypeToSet;
    }
}
