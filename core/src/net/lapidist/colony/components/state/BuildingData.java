package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

@KryoType
public final class BuildingData {
    private int x;
    private int y;
    private String buildingType;
    private String textureRef;

    public int getX() {
        return x;
    }

    public void setX(final int xToSet) {
        this.x = xToSet;
    }

    public int getY() {
        return y;
    }

    public void setY(final int yToSet) {
        this.y = yToSet;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(final String buildingTypeToSet) {
        this.buildingType = buildingTypeToSet;
    }

    public String getTextureRef() {
        return textureRef;
    }

    public void setTextureRef(final String textureRefToSet) {
        this.textureRef = textureRefToSet;
    }
}
