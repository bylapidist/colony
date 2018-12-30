package net.lapidist.colony.common.map.strategy;

import net.lapidist.colony.common.map.MapBuilder;
import net.lapidist.colony.common.map.tile.TileCoordinate;

import java.util.ArrayList;
import java.util.List;

public class RectangularMapLayoutStrategy extends MapLayoutStrategy {

    @Override
    public Iterable<TileCoordinate> fetchMapCoordinates(MapBuilder builder) {
        final List<TileCoordinate> coords = new ArrayList<>();

        for (int y = 0; y < builder.getGridHeight(); y++) {
            for (int x = 0; x < builder.getGridWidth(); x++) {
                coords.add(TileCoordinate.fromCoordinates(x, y, 0));
            }
        }

        return coords;
    }

    @Override
    public boolean checkParameters(int gridHeight, int gridWidth) {
        return checkCommonCase(gridHeight, gridWidth);
    }
}
