package net.lapidist.colony.grid.implementation;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.core.Camera;
import net.lapidist.colony.grid.*;
import net.lapidist.colony.grid.hex.CubeCoordinate;
import net.lapidist.colony.grid.hex.HexagonOrientation;
import net.lapidist.colony.grid.hex.IHexagon;
import net.lapidist.colony.grid.hex.ISatelliteData;
import net.lapidist.colony.grid.storage.IHexagonDataStorage;
import net.lapidist.colony.utils.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class HexagonImplementation<T extends ISatelliteData> implements IHexagon<T> {

    private final CubeCoordinate coordinate;
    private final transient List<Float> vertices;
    private final transient List<Vector2> points;
    private final transient Rectangle externalBoundingBox;
    private final transient Rectangle internalBoundingBox;
    private final transient Rectangle cameraBoundingBox;
    private final transient GridData sharedData;
    private final transient IHexagonDataStorage<T> hexagonDataStorage;

    HexagonImplementation(
        final GridData gridData,
        final CubeCoordinate coordinate,
        final IHexagonDataStorage<T> hexagonDataStorage
    ) {
        this.vertices = new ArrayList<>();
        this.sharedData = gridData;
        this.coordinate = coordinate;
        this.hexagonDataStorage = hexagonDataStorage;
        this.points = calculatePoints();

        final float x1 = points.get(3).x;
        final float y1 = points.get(2).y;
        final float x2 = points.get(0).x;
        final float y2 = points.get(5).y;
        final Vector2 camera1 = Camera.screenCoords(x1, y1);
        final Vector2 camera2 = Camera.screenCoords(x2, y2);

        this.externalBoundingBox = new Rectangle(x1, y1, x2 - x1, y2 - y1);
        this.internalBoundingBox = new Rectangle(
            getCenterX() - (1.25f * sharedData.getRadius() / 2),
            getCenterY() - (1.25f * sharedData.getRadius() / 2),
            1.25f * sharedData.getRadius(),
            1.25f * sharedData.getRadius()
        );
        this.cameraBoundingBox = new Rectangle(camera1.x, camera1.y, camera2.x - camera1.x, camera2.y - camera1.x);

        for (Vector2 point : points) {
            vertices.add(point.x);
            vertices.add(point.y);
        }
    }

    private List<Vector2> calculatePoints() {
        final List<Vector2> points = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            final float angle = 2 * (float) Math.PI / 6 * (i + sharedData.getOrientation().getCoordinateOffset());
            final float x = getCenterX() + sharedData.getRadius() * (float) cos(angle);
            final float y = getCenterY() + sharedData.getRadius() * (float) sin(angle);

            points.add(new Vector2(x, y));
        }

        return points;
    }

    @Override
    public String getId() {
        return coordinate.toAxialKey();
    }

    @Override
    public List<Vector2> getPoints() {
        return points;
    }

    @Override
    public float[] getVertices() {
        float[] floatVertices = new float[vertices.size()];

        int i = 0;
        for (Float v : vertices) {
            floatVertices[i++] = (v != null ? v : Float.NaN);
        }

        return floatVertices;
    }

    @Override
    public Rectangle getExternalBoundingBox() {
        return externalBoundingBox;
    }

    @Override
    public Rectangle getInternalBoundingBox() {
        return internalBoundingBox;
    }

    @Override
    public Rectangle getCameraBoundingBox() {
        return cameraBoundingBox;
    }

    @Override
    public CubeCoordinate getCubeCoordinate() {
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
        if (HexagonOrientation.FLAT_TOP.equals(sharedData.getOrientation())) {
            return coordinate.getGridX() * sharedData.getHexagonWidth() + sharedData.getRadius();
        }

        return coordinate.getGridX() * sharedData.getHexagonWidth() + coordinate.getGridZ()
            * sharedData.getHexagonWidth() / 2 + sharedData.getHexagonWidth() / 2;
    }

    @Override
    public float getCenterY() {
        if (HexagonOrientation.FLAT_TOP.equals(sharedData.getOrientation())) {
            return coordinate.getGridZ() * sharedData.getHexagonHeight() + coordinate.getGridX()
                * sharedData.getHexagonHeight() / 2 + sharedData.getHexagonHeight() / 2;
        }

        return coordinate.getGridZ() * sharedData.getHexagonHeight() + sharedData.getRadius();
    }

    @Override
    public Optional<T> getSatelliteData() {
        return hexagonDataStorage.getSatelliteDataBy(getCubeCoordinate());
    }

    @Override
    public void setSatelliteData(final T satelliteData) {
        hexagonDataStorage.addCoordinate(getCubeCoordinate(), satelliteData);
    }

    @Override
    public void clearSatelliteData() {
        this.hexagonDataStorage.clearDataFor(getCubeCoordinate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        final HexagonImplementation hexagon = (HexagonImplementation) obj;
        return Objects.equals(coordinate, hexagon.coordinate);
    }
}
