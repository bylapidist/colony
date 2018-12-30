package net.lapidist.colony.common.map.storage;

import net.lapidist.colony.common.map.tile.ITileMetaData;
import net.lapidist.colony.common.map.tile.TileCoordinate;

import java.util.Optional;

public interface ITileDataStorage<T extends ITileMetaData> {

    void addCoordinate(TileCoordinate coordinate);

    boolean addCoordinate(TileCoordinate coordinate, T metaData);

    Optional<T> getMetaDataBy(TileCoordinate coordinate);

    boolean hasDataFor(TileCoordinate coordinate);

    boolean containsCoordinate(TileCoordinate coordinate);

    Iterable<TileCoordinate> getCoordinates();

    boolean clearDataFor(TileCoordinate coordinate);
}
