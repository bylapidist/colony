package net.lapidist.colony.common.map.tile;

import com.badlogic.gdx.math.Rectangle;

import java.util.Optional;

public interface ITile<T extends ITileMetaData> {

    String getId();

    Rectangle getBoundingBox();

    TileCoordinate getTileCoordinate();

    int getGridX();

    int getGridY();

    int getGridZ();

    float getCenterX();

    float getCenterY();

    Optional<T> getMetaData();

    void setMetaData(T data);

    void clearMetaData();
}
