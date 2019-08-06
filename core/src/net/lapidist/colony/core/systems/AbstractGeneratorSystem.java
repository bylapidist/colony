package net.lapidist.colony.core.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.core.utils.noise.SimplexNoise;

public abstract class AbstractGeneratorSystem extends BaseSystem {

    private final int width;
    private final int height;
    private final String seed;
    protected int[][] map;

    public AbstractGeneratorSystem(String seed, int width, int height) {
        this.seed = seed;
        this.width = width;
        this.height = height;
        this.map = new int[width][height];

        SimplexNoise.seed(seed.hashCode());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getSeed() {
        return seed;
    }
}
