package net.lapidist.colony.common.grid.hex;

import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.common.grid.GridData;

import java.util.Collection;
import java.util.Optional;

public interface IHexagonalGrid<T extends ISatelliteData> {

    GridData getGridData();

    Iterable<IHexagon<T>> getHexagons();

    Iterable<IHexagon<T>> getHexagonsByCubeRange(CubeCoordinate from, CubeCoordinate to);

    Iterable<IHexagon<T>> getHexagonsByOffsetRange(int gridXFrom, int gridXTo, int gridYFrom, int gridYTo);

    boolean containsCubeCoordinate(CubeCoordinate coordinate);

    Optional<IHexagon<T>> getByCubeCoordinate(CubeCoordinate coordinate);

    Optional<IHexagon<T>> getByPixelCoordinate(Vector2 coordinate);

    Optional<IHexagon<T>> getNeighbourByIndex(IHexagon<T> hexagon, int index);

    Collection<IHexagon<T>> getNeighboursOf(IHexagon<T> hexagon);
}
