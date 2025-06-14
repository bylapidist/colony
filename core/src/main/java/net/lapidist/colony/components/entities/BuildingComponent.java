package net.lapidist.colony.components.entities;

import net.lapidist.colony.components.AbstractBoundedComponent;

public final class BuildingComponent extends AbstractBoundedComponent {
    private String buildingTypeId;
    private boolean dirty;

    public String getBuildingType() {
        return buildingTypeId;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(final boolean dirtyToSet) {
        this.dirty = dirtyToSet;
    }

    public void setBuildingType(final String buildingTypeToSet) {
        this.buildingTypeId = buildingTypeToSet;
    }
}
