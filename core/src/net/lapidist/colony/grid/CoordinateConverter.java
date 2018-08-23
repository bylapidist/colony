package net.lapidist.colony.grid;

import net.lapidist.colony.grid.hex.HexagonOrientation;

public final class CoordinateConverter {

    public static int convertOffsetCoordinatesToCubeX(final int offsetX, final int offsetY, final HexagonOrientation orientation) {
        return HexagonOrientation.FLAT_TOP.equals(orientation) ? offsetX : offsetX - offsetY / 2;
    }

    public static int convertOffsetCoordinatesToCubeZ(final int offsetX, final int offsetY, final HexagonOrientation orientation) {
        return HexagonOrientation.FLAT_TOP.equals(orientation) ? offsetY - offsetX / 2 : offsetY;
    }
}
