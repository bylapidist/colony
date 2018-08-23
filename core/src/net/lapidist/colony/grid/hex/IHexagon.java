package net.lapidist.colony.grid.hex;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.utils.Optional;

import java.util.List;

public interface IHexagon<T extends ISatelliteData> {

    String getId();

    List<Vector2> getPoints();

    float[] getVertices();

    Rectangle getExternalBoundingBox();

    Rectangle getInternalBoundingBox();

    Rectangle getCameraBoundingBox();

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
