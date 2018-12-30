package net.lapidist.colony.common.grid.hex;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.List;
import java.util.Optional;

public interface IHexagon<T extends ISatelliteData> {

    String getId();

    List<Vector2> getPoints();

    float[] getVertices();

    Rectangle getExternalBoundingBox();

    Rectangle getInternalBoundingBox();

    CubeCoordinate getCubeCoordinate();

    int getGridX();

    int getGridY();

    int getGridZ();

    float getCenterX();

    float getCenterY();

    Optional<T> getSatelliteData();

    void setSatelliteData(T data);

    void clearSatelliteData();
}
