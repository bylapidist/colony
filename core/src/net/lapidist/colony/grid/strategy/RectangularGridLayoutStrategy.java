package net.lapidist.colony.grid.strategy;

import net.lapidist.colony.grid.CoordinateConverter;
import net.lapidist.colony.grid.hex.CubeCoordinate;
import net.lapidist.colony.grid.GridBuilder;

import java.util.ArrayList;
import java.util.List;

public class RectangularGridLayoutStrategy extends GridLayoutStrategy {

    @Override
    public Iterable<CubeCoordinate> fetchGridCoordinates(GridBuilder builder) {
        final List<CubeCoordinate> coords = new ArrayList<>();
        for (int y = 0; y < builder.getGridHeight(); y++) {
            for (int x = 0; x < builder.getGridWidth(); x++) {
                final int gridX = CoordinateConverter.convertOffsetCoordinatesToCubeX(x, y, builder.getOrientation());
                final int gridZ = CoordinateConverter.convertOffsetCoordinatesToCubeZ(x, y, builder.getOrientation());
                coords.add(CubeCoordinate.fromCoordinates(gridX, gridZ));
            }
        }
        return coords;
    }

    @Override
    public boolean checkParameters(int gridHeight, int gridWidth) {
        return checkCommonCase(gridHeight, gridWidth);
    }
}
