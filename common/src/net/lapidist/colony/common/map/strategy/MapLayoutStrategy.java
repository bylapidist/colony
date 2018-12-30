package net.lapidist.colony.common.map.strategy;

import net.lapidist.colony.common.map.MapBuilder;
import net.lapidist.colony.common.map.tile.TileCoordinate;

public abstract class MapLayoutStrategy {

    public abstract Iterable<TileCoordinate> fetchMapCoordinates(MapBuilder builder);

    public abstract boolean checkParameters(final int gridHeight, final int gridWidth);

    protected final boolean checkCommonCase(final int gridHeight, final int gridWidth) {
        return gridHeight > 0 && gridWidth > 0;
    }
}
