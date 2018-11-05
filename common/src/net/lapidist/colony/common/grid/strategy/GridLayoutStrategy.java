package net.lapidist.colony.common.grid.strategy;

import net.lapidist.colony.common.grid.hex.CubeCoordinate;
import net.lapidist.colony.common.grid.GridBuilder;

public abstract class GridLayoutStrategy {

    public abstract Iterable<CubeCoordinate> fetchGridCoordinates(GridBuilder builder);

    public abstract boolean checkParameters(final int gridHeight, final int gridWidth);

    protected final boolean checkCommonCase(final int gridHeight, final int gridWidth) {
        return gridHeight > 0 && gridWidth > 0;
    }
}
