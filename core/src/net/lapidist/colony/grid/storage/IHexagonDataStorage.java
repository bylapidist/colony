package net.lapidist.colony.grid.storage;

import net.lapidist.colony.grid.hex.CubeCoordinate;
import net.lapidist.colony.grid.hex.ISatelliteData;
import net.lapidist.colony.utils.Optional;

public interface IHexagonDataStorage<T extends ISatelliteData> {

    void addCoordinate(CubeCoordinate cubeCoordinate);

    boolean addCoordinate(CubeCoordinate cubeCoordinate, T satelliteData);

    Optional<T> getSatelliteDataBy(CubeCoordinate cubeCoordinate);

    boolean containsCoordinate(CubeCoordinate cubeCoordinate);

    boolean hasDataFor(CubeCoordinate cubeCoordinate);

    Iterable<CubeCoordinate> getCoordinates();

    boolean clearDataFor(CubeCoordinate cubeCoordinate);
}
