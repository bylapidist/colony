package net.lapidist.colony.common.map;

import net.lapidist.colony.common.map.strategy.MapLayoutStrategy;
import net.lapidist.colony.common.map.strategy.RectangularMapLayoutStrategy;

public enum MapLayout {

    RECTANGULAR(new RectangularMapLayoutStrategy());

    private MapLayoutStrategy mapLayoutStrategy;

    MapLayout(final MapLayoutStrategy gridLayoutStrategy) {
        this.mapLayoutStrategy = gridLayoutStrategy;
    }

    boolean checkParameters(final int gridHeight, final int gridWidth) {
        return getMapLayoutStrategy().checkParameters(gridHeight, gridWidth);
    }

    public MapLayoutStrategy getMapLayoutStrategy() {
        return mapLayoutStrategy;
    }
}
