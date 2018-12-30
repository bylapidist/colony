package net.lapidist.colony.common.map;

import net.lapidist.colony.common.map.implementation.MapImplementation;
import net.lapidist.colony.common.map.storage.ITileDataStorage;
import net.lapidist.colony.common.map.storage.TileDataStorage;
import net.lapidist.colony.common.map.strategy.MapLayoutStrategy;
import net.lapidist.colony.common.map.tile.ITileGrid;
import net.lapidist.colony.common.map.tile.ITileMetaData;

public class MapBuilder<T extends ITileMetaData> {

    private int gridWidth;
    private int gridHeight;
    private int tileWidth;
    private int tileHeight;
    private ITileDataStorage tileDataStorage = new TileDataStorage();
    private MapLayout mapLayout = MapLayout.RECTANGULAR;

    public ITileGrid<T> build() {
        return new MapImplementation<>(this);
    }

    public MapData getMapData() {
        return new MapData(gridWidth, gridHeight, tileWidth, tileHeight);
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public MapBuilder setGridWidth(final int gridWidth) {
        this.gridWidth = gridWidth;
        return this;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public MapBuilder setGridHeight(final int gridHeight) {
        this.gridHeight = gridHeight;
        return this;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public MapBuilder setTileWidth(final int tileWidth) {
        this.tileWidth = tileWidth;
        return this;
    }

    public int getTileHeight() {
        return gridHeight;
    }

    public MapBuilder setTileHeight(final int tileHeight) {
        this.tileHeight = tileHeight;
        return this;
    }

    public MapLayoutStrategy getMapLayoutStrategy() {
        return mapLayout.getMapLayoutStrategy();
    }

    public ITileDataStorage<T> getTileDataStorage() {
        return tileDataStorage;
    }

    public MapBuilder setMapLayout(final MapLayout mapLayout) {
        this.mapLayout = mapLayout;
        return this;
    }
}
