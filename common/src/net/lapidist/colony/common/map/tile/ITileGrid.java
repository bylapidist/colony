package net.lapidist.colony.common.map.tile;

import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.common.map.MapData;

import java.util.Collection;
import java.util.Optional;

public interface ITileGrid<T extends ITileMetaData> {

    MapData getMapData();

    Iterable<ITile<T>> getTiles();

    Iterable<ITile<T>> getTilesByRange(TileCoordinate from, TileCoordinate to);

    Iterable<ITile<T>> getTilesByRange(int xFrom, int xTo, int yFrom, int yTo);

    boolean containsTileCoordinate(TileCoordinate coordinate);

    Optional<ITile<T>> getByTileCoordinate(TileCoordinate coordinate);

    Optional<ITile<T>> getByPixelCoordinate(Vector2 coordinate);

    Optional<ITile<T>> getNeighbourByIndex(ITile<T> tile, int index);

    Collection<ITile<T>> getNeighboursOf(ITile<T> tile);
}
