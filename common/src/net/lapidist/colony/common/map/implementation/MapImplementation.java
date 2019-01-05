package net.lapidist.colony.common.map.implementation;

import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.common.map.MapBuilder;
import net.lapidist.colony.common.map.MapData;
import net.lapidist.colony.common.map.storage.ITileDataStorage;
import net.lapidist.colony.common.map.tile.ITile;
import net.lapidist.colony.common.map.tile.ITileGrid;
import net.lapidist.colony.common.map.tile.ITileMetaData;
import net.lapidist.colony.common.map.tile.TileCoordinate;

import java.util.*;

public class MapImplementation<T extends ITileMetaData> implements ITileGrid<T> {

    private static final int[][] NEIGHBOURS = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};
    private static final int NEIGHBOUR_X_INDEX = 0;
    private static final int NEIGHBOUR_Y_INDEX = 1;
    private static final int NEIGHBOUR_Z_INDEX = 2;

    private final MapData mapData;
    private final ITileDataStorage<T> tileDataStorage;

    public MapImplementation(final MapBuilder<T> builder) {
        this.mapData = builder.getMapData();
        this.tileDataStorage = builder.getTileDataStorage();

        for (TileCoordinate tileCoordinate : builder.getMapLayoutStrategy().fetchMapCoordinates(builder)) {
            MapImplementation.this.tileDataStorage.addCoordinate(tileCoordinate);
        }
    }

    @Override
    public MapData getMapData() {
        return mapData;
    }

    @Override
    public Iterable<ITile<T>> getTiles() {
        final Iterator<TileCoordinate> coordIter = tileDataStorage.getCoordinates().iterator();

        return () -> new Iterator<ITile<T>>() {
            @Override
            public boolean hasNext() {
                return coordIter.hasNext();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("This is not a mutable iterator.");
            }

            @Override
            public ITile<T> next() {
                return new TileImplementation<>(mapData, coordIter.next(), tileDataStorage);
            }
        };
    }

    @Override
    public Iterable<ITile<T>> getTilesByRange(TileCoordinate from, TileCoordinate to) {
        final List<TileCoordinate> coordinates = new ArrayList<>();

        for (int gridZ = from.getGridY(); gridZ <= to.getGridY(); gridZ++) {
            for (int gridY = from.getGridY(); gridY <= to.getGridY(); gridY++) {
                for (int gridX = from.getGridX(); gridX <= to.getGridX(); gridX++) {
                    coordinates.add(TileCoordinate.fromCoordinates(gridX, gridY, gridZ));
                }
            }
        }

        coordinates.removeIf(this::containsTileCoordinate);

        final Iterator<TileCoordinate> coordIter = coordinates.iterator();

        return () -> new Iterator<ITile<T>>() {
            @Override
            public boolean hasNext() {
                return coordIter.hasNext();
            }

            @Override
            public ITile<T> next() {
                return getByTileCoordinate(coordIter.next()).get();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("This is not a mutable iterator.");
            }
        };
    }

    @Override
    public Iterable<ITile<T>> getTilesByRange(int xFrom, int xTo, int yFrom, int yTo) {
        // @TODO Unimplemented
        return null;
    }

    @Override
    public boolean containsTileCoordinate(TileCoordinate coordinate) {
        return tileDataStorage.containsCoordinate(coordinate);
    }

    @Override
    public Optional<ITile<T>> getByTileCoordinate(TileCoordinate coordinate) {
        return containsTileCoordinate(coordinate)
                ? Optional.of(new TileImplementation<>(mapData, coordinate, tileDataStorage))
                : Optional.empty();
    }

    @Override
    public Optional<ITile<T>> getByPixelCoordinate(Vector2 coordinate) {
        return Optional.empty();
    }

    @Override
    public Optional<ITile<T>> getNeighbourByIndex(ITile<T> tile, int index) {
        final int neighbourGridX = tile.getGridX() + NEIGHBOURS[index][NEIGHBOUR_X_INDEX];
        final int neighbourGridY = tile.getGridY() + NEIGHBOURS[index][NEIGHBOUR_Y_INDEX];
        final int neighbourGridZ = tile.getGridZ() + NEIGHBOURS[index][NEIGHBOUR_Z_INDEX];

        final TileCoordinate neighbourCoordinate = TileCoordinate.fromCoordinates(
                neighbourGridX,
                neighbourGridY,
                neighbourGridZ
        );

        return getByTileCoordinate(neighbourCoordinate);
    }

    @Override
    public Collection<ITile<T>> getNeighboursOf(ITile<T> tile) {
        final Set<ITile<T>> neighbors = new HashSet<>();

        for (int i = 0; i < NEIGHBOURS.length; i++) {
            final Optional<ITile<T>> retTile = getNeighbourByIndex(tile, i);

            retTile.ifPresent(neighbors::add);
        }

        return neighbors;
    }
}
