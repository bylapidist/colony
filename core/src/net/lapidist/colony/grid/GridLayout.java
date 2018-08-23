package net.lapidist.colony.grid;

import net.lapidist.colony.grid.strategy.GridLayoutStrategy;
import net.lapidist.colony.grid.strategy.RectangularGridLayoutStrategy;

public enum GridLayout {

    RECTANGULAR(new RectangularGridLayoutStrategy());

//    @TODO
//    HEXAGONAL(new HexagonalGridLayoutStrategy()),
//
//    TRIANGULAR(new TriangularGridLayoutStrategy()),
//
//    TRAPEZOID(new TrapezoidGridLayoutStrategy());

    private GridLayoutStrategy gridLayoutStrategy;

    GridLayout(final GridLayoutStrategy gridLayoutStrategy) {
        this.gridLayoutStrategy = gridLayoutStrategy;
    }

    boolean checkParameters(final int gridHeight, final int gridWidth) {
        return getGridLayoutStrategy().checkParameters(gridHeight, gridWidth);
    }

    public GridLayoutStrategy getGridLayoutStrategy() {
        return gridLayoutStrategy;
    }
}
