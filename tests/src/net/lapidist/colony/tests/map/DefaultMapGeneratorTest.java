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
    public void placesRandomResourceTiles() {
        DefaultMapGenerator generator = new DefaultMapGenerator();
        final int size = 10;
        MapState state = generator.generate(size, size);
        long resourceTiles = state.tiles().values().stream()
                .filter(t -> t.resources().wood() > 0 || t.resources().stone() > 0 || t.resources().food() > 0)
                .count();
        // ensure some but not all tiles contain resources
        assertTrue(resourceTiles > 0 && resourceTiles < size * size);
    }

    @Test
    public void generatesAllResourceTypes() {
        DefaultMapGenerator generator = new DefaultMapGenerator();
        final int size = 20;
        MapState state = generator.generate(size, size);
        boolean hasWood = state.tiles().values().stream().anyMatch(t -> t.resources().wood() > 0);
        boolean hasStone = state.tiles().values().stream().anyMatch(t -> t.resources().stone() > 0);
        boolean hasFood = state.tiles().values().stream().anyMatch(t -> t.resources().food() > 0);

        assertTrue(hasWood && hasStone && hasFood);
    }
}
