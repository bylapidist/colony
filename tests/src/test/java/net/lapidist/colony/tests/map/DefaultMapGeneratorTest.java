package net.lapidist.colony.tests.map;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.map.DefaultMapGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultMapGeneratorTest {

    @Test
    public void generatesExpectedTileCount() {
        DefaultMapGenerator generator = new DefaultMapGenerator();
        final int width = 4;
        final int height = 3;
        MapState state = generator.generate(width, height);
        assertEquals(width * height, state.tiles().size());
    }

    @Test
    public void generatesMixedTerrain() {
        DefaultMapGenerator generator = new DefaultMapGenerator();
        final int width = 10;
        final int height = 10;
        MapState state = generator.generate(width, height);
        long grass = state.tiles().values().stream()
                .filter(t -> "GRASS".equals(t.tileType()))
                .count();
        long dirt = state.tiles().values().stream()
                .filter(t -> "DIRT".equals(t.tileType()))
                .count();
        assertTrue(grass > 0);
        assertTrue(dirt > 0);
    }
}
