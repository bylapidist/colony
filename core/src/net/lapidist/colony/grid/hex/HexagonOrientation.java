package net.lapidist.colony.grid.hex;

public enum HexagonOrientation {

    POINTY_TOP(0.5f), FLAT_TOP(0);

    private float coordinateOffset;

    HexagonOrientation(final float coordinateOffset) {
        this.coordinateOffset = coordinateOffset;
    }

    public final float getCoordinateOffset() {
        return coordinateOffset;
    }
}
