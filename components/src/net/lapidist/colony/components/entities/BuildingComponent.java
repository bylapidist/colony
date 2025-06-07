package net.lapidist.colony.components.entities;

import com.artemis.Component;

public class BuildingComponent extends Component {

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
    private int x;
    private int y;
    private int width;
    private int height;
    private BuildingType buildingType;

    public final int getX() {
        return x;
    }

    public final void setX(final int xToSet) {
        this.x = xToSet;
    }

    public final int getY() {
        return y;
    }

    public final void setY(final int yToSet) {
        this.y = yToSet;
    }

    public final int getWidth() {
        return width;
    }

    public final void setWidth(final int widthToSet) {
        this.width = widthToSet;
    }

    public final int getHeight() {
        return height;
    }

    public final void setHeight(final int heightToSet) {
        this.height = heightToSet;
    }

    public final BuildingType getBuildingType() {
        return buildingType;
    }

    public final void setBuildingType(final BuildingType buildingTypeToSet) {
        this.buildingType = buildingTypeToSet;
    }
}
