package net.lapidist.colony.core.systems.abstracts;

import com.artemis.BaseSystem;
import net.lapidist.colony.core.utils.noise.SimplexNoise;

public abstract class AbstractGeneratorSystem extends BaseSystem {

    private final int width;
    private final int height;
    private final String seed;
    private int[][] map;

    public AbstractGeneratorSystem(
            final String seedToSet,
            final int widthToSet,
            final int heightToSet
    ) {
        this.seed = seedToSet;
        this.width = widthToSet;
        this.height = heightToSet;
        this.map = new int[widthToSet][heightToSet];

        SimplexNoise.seed(seed.hashCode());
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }

    public final String getSeed() {
        return seed;
    }

    public final int[][] getMap() {
        return map;
    }
}
