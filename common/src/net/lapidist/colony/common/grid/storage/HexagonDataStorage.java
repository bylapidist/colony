package net.lapidist.colony.common.grid.storage;

import net.lapidist.colony.common.grid.hex.CubeCoordinate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class HexagonDataStorage implements IHexagonDataStorage<SatelliteDataStorage> {

    private Map<CubeCoordinate, Optional<SatelliteDataStorage>> storage = new LinkedHashMap<>();

    @Override
    public void addCoordinate(final CubeCoordinate cubeCoordinate) {
        storage.put(cubeCoordinate, Optional.empty());
    }

    @Override
    public boolean addCoordinate(final CubeCoordinate cubeCoordinate, final SatelliteDataStorage satelliteData) {
        final Optional<SatelliteDataStorage> previous = storage.put(cubeCoordinate, Optional.of(satelliteData));
        return previous != null;
    }

    @Override
    public Optional<SatelliteDataStorage> getSatelliteDataBy(final CubeCoordinate cubeCoordinate) {
        return storage.containsKey(cubeCoordinate) ? storage.get(cubeCoordinate) : Optional.empty();
    }

    @Override
    public boolean containsCoordinate(final CubeCoordinate cubeCoordinate) {
        return storage.containsKey(cubeCoordinate);
    }

    @Override
    public boolean hasDataFor(final CubeCoordinate cubeCoordinate) {
        return storage.containsKey(cubeCoordinate) && storage.get(cubeCoordinate).isPresent();
    }

    @Override
    public Iterable<CubeCoordinate> getCoordinates() {
        return storage.keySet();
    }

    @Override
    public boolean clearDataFor(final CubeCoordinate cubeCoordinate) {
        boolean result = false;

        if (hasDataFor(cubeCoordinate)) {
            result = true;
        }

        storage.put(cubeCoordinate, Optional.empty());

        return result;
    }
}
