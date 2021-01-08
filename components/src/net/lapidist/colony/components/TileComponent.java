package net.lapidist.colony.components;

import com.artemis.Component;

public class TileComponent extends Component {

    private int x;
    private int y;
    private int z;
    private int width;
    private int height;
    private String texture;
    private boolean passable;
    private TileType tileType;

    public enum TileType {
        EMPTY("Empty"),
        GRASS("Grass");

        private final String type;

        TileType(final String typeToSet) {
            this.type = typeToSet;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    public final void setTile(
            final int xToSet,
            final int yToSet,
            final int zToSet,
            final int widthToSet,
            final int heightToSet,
            final String textureToSet,
            final boolean passableToSet,
            final TileType tileTypeToSet
    ) {
        setX(xToSet);
        setY(yToSet);
        setZ(zToSet);
        setWidth(widthToSet);
        setHeight(heightToSet);
        setTexture(textureToSet);
        setPassable(passableToSet);
        setTileType(tileTypeToSet);
    }

    public final int getX() {
        return x;
    }

    public final void setX(final int xToSet) {
        this.x = xToSet;
    }

    public final int getY() {
        return y;
    }

    public final int getZ() {
        return z;
    }

    public final void setZ(final int zToSet) {
        this.z = zToSet;
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

    public final String getTexture() {
        return texture;
    }

    public final void setTexture(final String textureToSet) {
        this.texture = textureToSet;
    }

    public final boolean isPassable() {
        return passable;
    }

    public final void setPassable(final boolean passableToSet) {
        this.passable = passableToSet;
    }

    public final TileType getTileType() {
        return tileType;
    }

    public final void setTileType(final TileType tileTypeToSet) {
        this.tileType = tileTypeToSet;
    }
}
