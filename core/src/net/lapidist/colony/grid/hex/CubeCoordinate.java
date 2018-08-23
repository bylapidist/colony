package net.lapidist.colony.grid.hex;

import java.util.Objects;

public final class CubeCoordinate {

    private final int gridX;
    private final int gridZ;

    public CubeCoordinate(final int gridX, final int gridZ) {
        this.gridX = gridX;
        this.gridZ = gridZ;
    }

    public static CubeCoordinate fromAxialKey(final String axialKey) {
        CubeCoordinate result;

        try {
            final String[] coords = axialKey.split(",");
            result = fromCoordinates(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
        } catch (final Exception e) {
            throw new IllegalArgumentException("Failed to create CubeCoordinate from: " + axialKey, e);
        }

        return result;
    }

    public static CubeCoordinate fromCoordinates(final int gridX, final int gridZ) {
        return new CubeCoordinate(gridX, gridZ);
    }

    public String toAxialKey() {
        return gridX + "," + gridZ;
    }

    public int getGridY() {
        return -(getGridX() + getGridZ());
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridZ() {
        return gridZ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gridX, gridZ);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final CubeCoordinate that = (CubeCoordinate) obj;

        return gridX == that.getGridX() && gridZ == that.getGridZ();
    }
}
