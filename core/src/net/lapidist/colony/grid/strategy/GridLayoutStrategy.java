package net.lapidist.colony.grid.strategy;

import net.lapidist.colony.grid.hex.CubeCoordinate;
import net.lapidist.colony.grid.GridBuilder;

public abstract class GridLayoutStrategy {

    public abstract Iterable<CubeCoordinate> fetchGridCoordinates(GridBuilder builder);

    public abstract boolean checkParameters(final int gridHeight, final int gridWidth);

    protected final boolean checkCommonCase(final int gridHeight, final int gridWidth) {
        return gridHeight > 0 && gridWidth > 0;
    }
}
