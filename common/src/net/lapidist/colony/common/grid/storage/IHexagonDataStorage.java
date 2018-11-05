package net.lapidist.colony.common.grid.storage;

import net.lapidist.colony.common.grid.hex.CubeCoordinate;
import net.lapidist.colony.common.grid.hex.ISatelliteData;
import net.lapidist.colony.common.utils.Optional;

public interface IHexagonDataStorage<T extends ISatelliteData> {

    void addCoordinate(CubeCoordinate cubeCoordinate);

    boolean addCoordinate(CubeCoordinate cubeCoordinate, T satelliteData);

    Optional<T> getSatelliteDataBy(CubeCoordinate cubeCoordinate);

    boolean containsCoordinate(CubeCoordinate cubeCoordinate);

    boolean hasDataFor(CubeCoordinate cubeCoordinate);

    Iterable<CubeCoordinate> getCoordinates();

    boolean clearDataFor(CubeCoordinate cubeCoordinate);
}
