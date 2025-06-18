package net.lapidist.colony.tests.map;

import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.map.ChunkPos;
import net.lapidist.colony.map.ChunkedMapGenerator;
import net.lapidist.colony.map.MapChunkData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChunkedMapGeneratorTest {

    private static final int DEFAULT_WOOD = 10;

    @Test
    public void generatesExpectedTileCount() {
        ChunkedMapGenerator generator = new ChunkedMapGenerator();
        final int width = 4;
        final int height = 3;
        MapState state = generator.generate(width, height);
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                state.getTile(x, y);
                count++;
            }
        }
        assertEquals(width * height, count);
    }

    @Test
    public void generatesMixedTerrain() {
        ChunkedMapGenerator generator = new ChunkedMapGenerator();
        final int width = 10;
        final int height = 10;
        MapState state = generator.generate(width, height);
        long grass = 0;
        long dirt = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                String type = state.getTile(x, y).tileType();
                if ("GRASS".equals(type)) {
                    grass++;
                } else if ("DIRT".equals(type)) {
                    dirt++;
                }
            }
        }
        assertTrue(grass > 0);
        assertTrue(dirt > 0);
    }

    @Test
    public void resourcesGeneratedOnDemand() {
        ChunkedMapGenerator generator = new ChunkedMapGenerator();
        MapState state = generator.generate(2, 2);
        MapChunkData chunk = state.chunks().get(new ChunkPos(0, 0));
        assertTrue(chunk.getTiles().isEmpty());

        var tile = state.getTile(0, 0);
        assertEquals(DEFAULT_WOOD, tile.resources().wood());
    }
}
