package net.lapidist.colony.client;

/** Utility for determining pseudo-random tile rotations. */
public final class TileRotationUtil {

    private static final int ROTATION_STEPS = 4;
    private static final float ROTATION_ANGLE = 90f;
    private static final int ROTATION_SEED_X = 31;
    private static final int ROTATION_SEED_Y = 17;
    private static final int HASH_SHIFT_1 = 16;
    private static final int HASH_SHIFT_2 = 13;
    private static final int HASH_MULT_1 = 0x85ebca6b;
    private static final int HASH_MULT_2 = 0xc2b2ae35;

    private TileRotationUtil() {
    }

    /**
     * Calculate the rotation step index for the given coordinates.
     *
     * @param x tile x coordinate
     * @param y tile y coordinate
     * @return rotation index in the range {@code 0-3}
     */
    public static int indexFor(final int x, final int y) {
        int hash = x * ROTATION_SEED_X ^ y * ROTATION_SEED_Y;
        hash = (hash ^ (hash >>> HASH_SHIFT_1)) * HASH_MULT_1;
        hash = (hash ^ (hash >>> HASH_SHIFT_2)) * HASH_MULT_2;
        hash ^= (hash >>> HASH_SHIFT_1);
        return hash & (ROTATION_STEPS - 1);
    }

    /**
     * Return a stable rotation angle in degrees for the given tile coordinates.
     *
     * @param x tile x coordinate
     * @param y tile y coordinate
     * @return rotation angle
     */
    public static float rotationFor(final int x, final int y) {
        return indexFor(x, y) * ROTATION_ANGLE;
    }
}
