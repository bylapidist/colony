package net.lapidist.colony.map;

import java.util.Random;

/**
 * Simple 2D Perlin noise generator based on Ken Perlin's reference
 * implementation.
 */
public final class PerlinNoise {

    private static final int TABLE_SIZE = 256;
    private static final int TABLE_MASK = TABLE_SIZE - 1;
    private static final int PERM_SIZE = TABLE_SIZE * 2;
    private static final int GRADIENT_MASK = 3;
    private static final double FADE_A = 6.0;
    private static final double FADE_B = 15.0;
    private static final double FADE_C = 10.0;

    private final int[] perm = new int[PERM_SIZE];

    public PerlinNoise(final long seed) {
        int[] p = new int[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++) {
            p[i] = i;
        }
        Random random = new Random(seed);
        for (int i = TABLE_MASK; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = p[i];
            p[i] = p[j];
            p[j] = tmp;
        }
        for (int i = 0; i < PERM_SIZE; i++) {
            perm[i] = p[i & TABLE_MASK];
        }
    }

    public double noise(final double x, final double y) {
        int xi = fastFloor(x) & TABLE_MASK;
        int yi = fastFloor(y) & TABLE_MASK;
        double xf = x - fastFloor(x);
        double yf = y - fastFloor(y);

        double u = fade(xf);
        double v = fade(yf);

        int aa = perm[xi] + yi;
        int ab = perm[xi] + yi + 1;
        int ba = perm[xi + 1] + yi;
        int bb = perm[xi + 1] + yi + 1;

        double x1 = lerp(u, grad(perm[aa], xf, yf), grad(perm[ba], xf - 1, yf));
        double x2 = lerp(u, grad(perm[ab], xf, yf - 1), grad(perm[bb], xf - 1, yf - 1));

        return lerp(v, x1, x2);
    }

    private static int fastFloor(final double x) {
        int xi = (int) x;
        return x < xi ? xi - 1 : xi;
    }

    private static double fade(final double t) {
        return t * t * t * (t * (t * FADE_A - FADE_B) + FADE_C);
    }

    private static double lerp(final double t, final double a, final double b) {
        return a + t * (b - a);
    }

    private static double grad(final int hash, final double x, final double y) {
        int h = hash & GRADIENT_MASK;
        double u = h < 2 ? x : y;
        double v = h < 2 ? y : x;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}
