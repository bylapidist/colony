package net.lapidist.colony.common.grid.implementation;

import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.common.grid.CoordinateConverter;
import net.lapidist.colony.common.grid.GridData;
import net.lapidist.colony.common.grid.GridBuilder;
import net.lapidist.colony.common.grid.storage.IHexagonDataStorage;
import net.lapidist.colony.common.grid.hex.CubeCoordinate;
import net.lapidist.colony.common.grid.hex.IHexagon;
import net.lapidist.colony.common.grid.hex.IHexagonalGrid;
import net.lapidist.colony.common.grid.hex.ISatelliteData;
import net.lapidist.colony.common.utils.Optional;

import java.util.*;

public final class GridImplementation<T extends ISatelliteData> implements IHexagonalGrid<T> {

    private static final int[][] NEIGHBORS = {{+1, 0}, {+1, -1}, {0, -1}, {-1, 0}, {-1, +1}, {0, +1}};
    private static final int NEIGHBOR_X_INDEX = 0;
    private static final int NEIGHBOR_Z_INDEX = 1;

    private final GridData gridData;
    private final IHexagonDataStorage<T> hexagonDataStorage;

    public GridImplementation(final GridBuilder<T> builder) {
        this.gridData = builder.getGridData();
        this.hexagonDataStorage = builder.getHexagonDataStorage();

        for (CubeCoordinate cubeCoordinate : builder.getGridLayoutStrategy().fetchGridCoordinates(builder)) {
            GridImplementation.this.hexagonDataStorage.addCoordinate(cubeCoordinate);
        }
    }

    @Override
    public GridData getGridData() {
        return gridData;
    }

    @Override
    public Iterable<IHexagon<T>> getHexagons() {
        final Iterator<CubeCoordinate> coordIter = hexagonDataStorage.getCoordinates().iterator();

        return () -> new Iterator<IHexagon<T>>() {
            @Override
            public boolean hasNext() {
                return coordIter.hasNext();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("This is not a mutable iterator.");
            }

            @Override
            public IHexagon<T> next() {
                return new HexagonImplementation<>(gridData, coordIter.next(), hexagonDataStorage);
            }
        };
    }

    @Override
    public Iterable<IHexagon<T>> getHexagonsByCubeRange(final CubeCoordinate from, final CubeCoordinate to) {
        final List<CubeCoordinate> coordinates = new ArrayList<>();

        for (int gridZ = from.getGridZ(); gridZ <= to.getGridZ(); gridZ++) {
            for (int gridX = from.getGridX(); gridX <= to.getGridX(); gridX++) {
                coordinates.add(CubeCoordinate.fromCoordinates(gridX, gridZ));
            }
        }

        final Iterator<CubeCoordinate> iter = coordinates.iterator();

        while (iter.hasNext()) {
            CubeCoordinate next = iter.next();

            if (containsCubeCoordinate(next)) {
                iter.remove();
            }
        }

        final Iterator<CubeCoordinate> coordIter = coordinates.iterator();

        return () -> new Iterator<IHexagon<T>>() {
            @Override
            public boolean hasNext() {
                return coordIter.hasNext();
            }

            @Override
            public IHexagon<T> next() {
                return getByCubeCoordinate(coordIter.next()).get();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("This is not a mutable iterator.");
            }
        };
    }

    @Override
    public Iterable<IHexagon<T>> getHexagonsByOffsetRange(final int gridXFrom, final int gridXTo, final int gridYFrom, final int gridYTo) {
        final List<CubeCoordinate> coords = new ArrayList<>();

        for (int gridX = gridXFrom; gridX <= gridXTo; gridX++) {
            for (int gridY = gridYFrom; gridY <= gridYTo; gridY++) {
                final int cubeX = CoordinateConverter.convertOffsetCoordinatesToCubeX(gridX, gridY, gridData.getOrientation());
                final int cubeZ = CoordinateConverter.convertOffsetCoordinatesToCubeZ(gridX, gridY, gridData.getOrientation());
                CubeCoordinate coord = CubeCoordinate.fromCoordinates(cubeX, cubeZ);
                if (getByCubeCoordinate(coord).isPresent()) {
                    coords.add(coord);
                }
            }
        }

        final Iterator<CubeCoordinate> coordIter = coords.iterator();

        return () -> new Iterator<IHexagon<T>>() {
            @Override
            public boolean hasNext() {
                return coordIter.hasNext();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("This is not a mutable iterator.");
            }

            @Override
            public IHexagon<T> next() {
                return getByCubeCoordinate(coordIter.next()).get();
            }
        };
    }

    @Override
    public boolean containsCubeCoordinate(CubeCoordinate coordinate) {
        return this.hexagonDataStorage.containsCoordinate(coordinate);
    }

    @Override
    public Optional<IHexagon<T>> getByCubeCoordinate(CubeCoordinate coordinate) {
        return containsCubeCoordinate(coordinate)
                ? Optional.of(new HexagonImplementation<>(gridData, coordinate, hexagonDataStorage))
                : Optional.empty();
    }

    @Override
    public Optional<IHexagon<T>> getByPixelCoordinate(Vector2 coordinate) {
        int estimatedGridX = (int) (coordinate.x / gridData.getHexagonWidth());
        int estimatedGridZ = (int) (coordinate.y / gridData.getHexagonHeight());

        estimatedGridX = CoordinateConverter.convertOffsetCoordinatesToCubeX(estimatedGridX, estimatedGridZ, gridData.getOrientation());
        estimatedGridZ = CoordinateConverter.convertOffsetCoordinatesToCubeZ(estimatedGridX, estimatedGridZ, gridData.getOrientation());

        // it is possible that the estimated coordinates are off the grid so we
        // create a virtual hexagon
        final CubeCoordinate estimatedCoordinate = CubeCoordinate.fromCoordinates(estimatedGridX, estimatedGridZ);
        final IHexagon tempHex = new HexagonImplementation(gridData, estimatedCoordinate, hexagonDataStorage);
        final IHexagon trueHex = refineHexagonByPixel(tempHex, new Vector2(coordinate.x, coordinate.y));

        if (hexagonsAreAtTheSamePosition(tempHex, trueHex)) {
            return getByCubeCoordinate(estimatedCoordinate);
        }

        return containsCubeCoordinate(trueHex.getCubeCoordinate()) ? Optional.of(trueHex) : Optional.empty();
    }

    @Override
    public Optional<IHexagon<T>> getNeighbourByIndex(IHexagon<T> hexagon, int index) {
        final int neighborGridX = hexagon.getGridX() + NEIGHBORS[index][NEIGHBOR_X_INDEX];
        final int neighborGridZ = hexagon.getGridZ() + NEIGHBORS[index][NEIGHBOR_Z_INDEX];
        final CubeCoordinate neighborCoordinate = CubeCoordinate.fromCoordinates(neighborGridX, neighborGridZ);
        return getByCubeCoordinate(neighborCoordinate);
    }

    @Override
    public Collection<IHexagon<T>> getNeighboursOf(IHexagon<T> hexagon) {
        final Set<IHexagon<T>> neighbors = new HashSet<>();
        for (int i = 0; i < NEIGHBORS.length; i++) {
            final Optional<IHexagon<T>> retHex = getNeighbourByIndex(hexagon, i);
            if (retHex.isPresent()) {
                neighbors.add(retHex.get());
            }
        }
        return neighbors;
    }

    private boolean hexagonsAreAtTheSamePosition(final IHexagon<T> hex0, final IHexagon<T> hex1) {
        return hex0.getGridX() == hex1.getGridX() && hex0.getGridZ() == hex1.getGridZ();
    }

    private IHexagon<T> refineHexagonByPixel(final IHexagon<T> hexagon, final Vector2 clickedPoint) {
        IHexagon refined = hexagon;
        double smallestDistance = Math.sqrt(Math.pow((refined.getCenterX() - clickedPoint.x), 2) + Math.pow((refined.getCenterY() - clickedPoint.y), 2));

        for (final IHexagon<T> neighbour : getNeighboursOf(hexagon)) {
            final double currentDistance = Math.sqrt(Math.pow((neighbour.getCenterX() - clickedPoint.x), 2) + Math.pow((neighbour.getCenterY() - clickedPoint.y), 2));

            if (currentDistance < smallestDistance) {
                refined = neighbour;
                smallestDistance = currentDistance;
            }
        }

        return refined;
    }
}
