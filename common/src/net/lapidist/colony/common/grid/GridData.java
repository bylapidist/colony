package net.lapidist.colony.common.grid;

import net.lapidist.colony.common.grid.hex.HexagonOrientation;

import static java.lang.Math.sqrt;

public final class GridData {

    private final HexagonOrientation orientation;
    private final GridLayout gridLayout;
    private final float radius;
    private final float hexagonHeight;
    private final float hexagonWidth;
    private final int gridWidth;
    private final int gridHeight;

    public GridData(
            final HexagonOrientation orientation,
            final GridLayout gridLayout,
            final float radius,
            final int gridWidth,
            final int gridHeight
    ) {
        this.orientation = orientation;
        this.gridLayout = gridLayout;
        this.radius = radius;
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        this.hexagonHeight = HexagonOrientation.FLAT_TOP.equals(orientation)
            ? calculateHeight(radius)
            : calculateWidth(radius);
        this.hexagonWidth = HexagonOrientation.FLAT_TOP.equals(orientation)
            ? calculateWidth(radius)
            : calculateHeight(radius);
    }

    private static float calculateHeight(final float radius) {
        return (float) sqrt(3) * radius;
    }

    private static float calculateWidth(final float radius) {
        return radius * 3 / 2;
    }

    public GridLayout getGridLayout() {
        return gridLayout;
    }

    public HexagonOrientation getOrientation() {
        return orientation;
    }

    public float getRadius() {
        return radius;
    }

    public float getHexagonHeight() {
        return hexagonHeight;
    }

    public float getHexagonWidth() {
        return hexagonWidth;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }
}
