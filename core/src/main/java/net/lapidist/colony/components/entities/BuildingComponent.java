package net.lapidist.colony.components.entities;

import net.lapidist.colony.components.AbstractBoundedComponent;
import net.lapidist.colony.i18n.I18n;

public final class BuildingComponent extends AbstractBoundedComponent {

    public enum BuildingType {
        HOUSE("building.house"),
        MARKET("building.market"),
        FACTORY("building.factory");

        private final String key;

        BuildingType(final String keyToSet) {
            this.key = keyToSet;
        }

        @Override
        public String toString() {
            return I18n.get(key);
        }
    }
    private BuildingType buildingType;
    private boolean dirty;

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(final boolean dirtyToSet) {
        this.dirty = dirtyToSet;
    }

    public void setBuildingType(final BuildingType buildingTypeToSet) {
        this.buildingType = buildingTypeToSet;
    }
}
