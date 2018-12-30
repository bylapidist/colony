package net.lapidist.colony.common.map.tile;

import java.util.Objects;

public class TileCoordinate {

    private final int gridX;
    private final int gridY;
    private final int gridZ;

    public TileCoordinate(final int gridX, final int gridY, final int gridZ) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.gridZ = gridZ;
    }

    public static TileCoordinate fromAxialKey(final String axialKey) {
        TileCoordinate result;

        try {
            final String[] coords = axialKey.split(",");
            result = fromCoordinates(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
        } catch (final Exception e) {
            throw new IllegalArgumentException("Failed to create TileCoordinate from: " + axialKey, e);
        }

        return result;
    }

    public static TileCoordinate fromCoordinates(final int gridX, final int gridY, final int gridZ) {
        return new TileCoordinate(gridX, gridY, gridZ);
    }

    public String toAxialKey() {
        return gridX + "," + gridY + "," + gridZ;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
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

        final TileCoordinate that = (TileCoordinate) obj;

        return gridX == that.getGridX() && gridY == that.getGridY() && gridZ == that.getGridZ();
    }
}
