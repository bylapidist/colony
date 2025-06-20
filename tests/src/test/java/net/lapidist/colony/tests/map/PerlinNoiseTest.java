package net.lapidist.colony.tests.map;

import net.lapidist.colony.map.PerlinNoise;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PerlinNoiseTest {

    private static final long SEED_ONE = 42L;
    private static final long SEED_TWO = 123L;
    private static final double X_POS = 1.5;
    private static final double Y_POS = 2.5;
    private static final double X2 = 0.2;
    private static final double Y2 = 0.8;
    private static final double EPSILON = 1e-6;

    @Test
    public void sameSeedProducesSameValues() {
        PerlinNoise a = new PerlinNoise(SEED_ONE);
        PerlinNoise b = new PerlinNoise(SEED_ONE);
        double va = a.noise(X_POS, Y_POS);
        double vb = b.noise(X_POS, Y_POS);
        assertEquals(va, vb, EPSILON);
    }

    @Test
    public void noiseWithinExpectedRange() {
        PerlinNoise noise = new PerlinNoise(SEED_TWO);
        double value = noise.noise(X2, Y2);
        assertTrue(value >= -1.0 && value <= 1.0);
    }
}
