package net.lapidist.colony.common.map.storage;

import net.lapidist.colony.common.map.tile.TileCoordinate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class TileDataStorage implements ITileDataStorage<TileMetaDataStorage> {

    private Map<TileCoordinate, Optional<TileMetaDataStorage>> storage = new LinkedHashMap<>();

    @Override
    public void addCoordinate(TileCoordinate coordinate) {
        storage.put(coordinate, Optional.empty());
    }

    @Override
    public boolean addCoordinate(TileCoordinate coordinate, TileMetaDataStorage metaData) {
        final Optional<TileMetaDataStorage> previous = storage.put(coordinate, Optional.of(metaData));
        return previous != null;
    }

    @Override
    public Optional<TileMetaDataStorage> getMetaDataBy(TileCoordinate coordinate) {
        return storage.containsKey(coordinate) ? storage.get(coordinate) : Optional.empty();
    }

    @Override
    public boolean containsCoordinate(TileCoordinate coordinate) {
        return storage.containsKey(coordinate);
    }

    @Override
    public Iterable<TileCoordinate> getCoordinates() {
        return storage.keySet();
    }

    @Override
    public boolean hasDataFor(TileCoordinate coordinate) {
        return storage.containsKey(coordinate) && storage.get(coordinate).isPresent();
    }

    @Override
    public boolean clearDataFor(TileCoordinate coordinate) {
        boolean result = false;

        if (hasDataFor(coordinate)) {
            result = true;
        }

        storage.put(coordinate, Optional.empty());

        return result;
    }
}
