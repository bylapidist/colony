package net.lapidist.colony.components.state;

import java.io.Serial;
import java.io.Serializable;

public final class TileData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int x;
    private int y;
    private String tileType;
    private String textureRef;
    private boolean passable;

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

    public String getTileType() {
        return tileType;
    }

    public void setTileType(final String tileTypeToSet) {
        this.tileType = tileTypeToSet;
    }

    public String getTextureRef() {
        return textureRef;
    }

    public void setTextureRef(final String textureRefToSet) {
        this.textureRef = textureRefToSet;
    }

    public boolean isPassable() {
        return passable;
    }

    public void setPassable(final boolean passableToSet) {
        this.passable = passableToSet;
    }
}
