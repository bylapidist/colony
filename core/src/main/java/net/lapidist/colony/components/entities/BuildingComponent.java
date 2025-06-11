package net.lapidist.colony.components.entities;

import net.lapidist.colony.components.AbstractBoundedComponent;

public class BuildingComponent extends AbstractBoundedComponent {

    public enum BuildingType {
        HOUSE("House"),
        MARKET("Market"),
        FACTORY("Factory");

        private final String type;

        BuildingType(final String typeToSet) {
            this.type = typeToSet;
        }

        @Override
        public String toString() {
            return type;
        }
    }
    private BuildingType buildingType;

    public final BuildingType getBuildingType() {
        return buildingType;
    }

    public final void setBuildingType(final BuildingType buildingTypeToSet) {
        this.buildingType = buildingTypeToSet;
    }
}
