package net.lapidist.colony.common.map;

import com.badlogic.gdx.math.Rectangle;

public class MapData {

    private final int gridWidth;
    private final int gridHeight;
    private final int tileWidth;
    private final int tileHeight;
    private final Rectangle bounds;

    public MapData(
            final int gridWidth,
            final int gridHeight,
            final int tileWidth,
            final int tileHeight
    ) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.bounds = new Rectangle(
                0,
                0,
                getGridWidth() * getTileWidth(),
                getGridHeight() * getTileHeight()
        );
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
