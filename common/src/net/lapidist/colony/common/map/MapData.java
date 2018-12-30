package net.lapidist.colony.common.map;

public class MapData {

    private final int gridWidth;
    private final int gridHeight;
    private final int tileWidth;
    private final int tileHeight;

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
}
