package net.lapidist.colony.tests.map;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.map.DefaultMapGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultMapGeneratorTest {

    @Test
    public void generatesExpectedTileCount() {
        DefaultMapGenerator generator = new DefaultMapGenerator();
        final int width = 4;
        final int height = 3;
        MapState state = generator.generate(width, height);
        assertEquals(width * height, state.getTiles().size());
    }
}
