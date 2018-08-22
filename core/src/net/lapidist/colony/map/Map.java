package net.lapidist.colony.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import net.lapidist.colony.Colony;

public class Map {

    public Colony game;

    // Tile constants
    public static final int TILE_WIDTH = 128;
    public static final int TILE_HEIGHT = 128;
    public static final int TILE_HEIGHT_DIAMOND = TILE_HEIGHT / 2;

    // Generator types
    public static final int FLAT = 0;
    public static final int RANDOM = 1;
    public static final int STANDARD = 2;

    private int width, height, depth;
    private int boundX, boundY;
    private long seed;

    /**
     * Constructor for Map.
     */
    public Map(int width, int height, int depth) {
        this.game = Colony.instance();
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.seed = TimeUtils.nanoTime();

        boundX = width * TILE_WIDTH;
        boundY = height * TILE_HEIGHT_DIAMOND;

        //boundX = height * TILE_WIDTH / 2 + width * TILE_WIDTH / 2;
        //boundY = height * TILE_HEIGHT_DIAMOND / 2 + width * TILE_HEIGHT_DIAMOND / 2;
    }

    /**
     * Convert tile coords to screen coords.
     * @param x x coord.
     * @param y y coord.
     * @return Vector2 screen coords.
     */
    public static Vector2 toScreenCoords(float x, float y) {
        float sx = (y * TILE_WIDTH / 2) + (x * TILE_WIDTH / 2);
        float sy = (x * TILE_HEIGHT_DIAMOND / 2) - (y * TILE_HEIGHT_DIAMOND / 2);

        return new Vector2(sx, sy);
    }

    /**
     * @return The width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return The height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return The depth.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @return The boundX.
     */
    public int getBoundX() {
        return boundX;
    }

    /**
     * @return The boundY.
     */
    public int getBoundY() {
        return boundY;
    }

    /**
     * @return The seed.
     */
    public long getSeed() {
        return seed;
    }
}
