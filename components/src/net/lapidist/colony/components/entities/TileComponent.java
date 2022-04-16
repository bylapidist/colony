package net.lapidist.colony.components.entities;

import com.badlogic.ashley.core.Component;

public class TileComponent implements Component {

    public enum TileType {
        EMPTY("Empty");

        private final String type;

        TileType(final String typeToSet) {
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

    private boolean passable;

    private TileType tileType;

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }

    public final boolean isPassable() {
        return passable;
    }

    public final TileType getTileType() {
        return tileType;
    }

    public final void setX(final int xToSet) {
        this.x = xToSet;
    }

    public final void setY(final int yToSet) {
        this.y = yToSet;
    }

    public final void setWidth(final int widthToSet) {
        this.width = widthToSet;
    }

    public final void setHeight(final int heightToSet) {
        this.height = heightToSet;
    }

    public final void setPassable(final boolean passableToSet) {
        this.passable = passableToSet;
    }

    public final void setTileType(final TileType tileTypeToSet) {
        this.tileType = tileTypeToSet;
    }
}
