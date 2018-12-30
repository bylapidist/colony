package net.lapidist.colony.common.map.implementation;

import com.badlogic.gdx.math.Rectangle;
import net.lapidist.colony.common.map.MapData;
import net.lapidist.colony.common.map.storage.ITileDataStorage;
import net.lapidist.colony.common.map.tile.ITile;
import net.lapidist.colony.common.map.tile.ITileMetaData;
import net.lapidist.colony.common.map.tile.TileCoordinate;

import java.util.Objects;
import java.util.Optional;

public class TileImplementation<T extends ITileMetaData> implements ITile<T> {

    private final TileCoordinate coordinate;
    private final transient Rectangle boundingBox;
    private final transient MapData sharedData;
    private final transient ITileDataStorage<T> tileDataStorage;

    TileImplementation(
        final MapData mapData,
        final TileCoordinate coordinate,
        final ITileDataStorage<T> tileDataStorage
    ) {
        this.sharedData = mapData;
        this.coordinate = coordinate;
        this.tileDataStorage = tileDataStorage;
        this.boundingBox = new Rectangle(
            coordinate.getGridX() * sharedData.getTileWidth(),
            coordinate.getGridY() * sharedData.getTileHeight(),
            sharedData.getTileWidth(),
            sharedData.getTileHeight()
        );
    }

    @Override
    public String getId() {
        return coordinate.toAxialKey();
    }

    @Override
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    @Override
    public TileCoordinate getTileCoordinate() {
        return coordinate;
    }

    @Override
    public int getGridX() {
        return coordinate.getGridX();
    }

    @Override
    public int getGridY() {
        return coordinate.getGridY();
    }

    @Override
    public int getGridZ() {
        return coordinate.getGridZ();
    }

    @Override
    public float getCenterX() {
        return (coordinate.getGridX() * sharedData.getTileWidth())
                + (sharedData.getTileWidth() / 2f);
    }

    @Override
    public float getCenterY() {
        return (coordinate.getGridY() * sharedData.getTileHeight())
                + (sharedData.getTileHeight() / 2f);
    }

    @Override
    public Optional<T> getMetaData() {
        return tileDataStorage.getMetaDataBy(getTileCoordinate());
    }

    @Override
    public void setMetaData(T data) {
        tileDataStorage.addCoordinate(getTileCoordinate(), data);
    }

    @Override
    public void clearMetaData() {
        this.tileDataStorage.clearDataFor(getTileCoordinate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        final TileImplementation tile = (TileImplementation) obj;
        return Objects.equals(coordinate, tile.coordinate);
    }
}
