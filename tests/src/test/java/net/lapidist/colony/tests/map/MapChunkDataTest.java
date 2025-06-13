package net.lapidist.colony.tests.map;

import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.components.state.TileData;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapChunkDataTest {

    private static final long SEED_ONE = 42L;
    private static final long SEED_TWO = 123L;
    private static final int SAMPLE_X = 5;
    private static final int SAMPLE_Y = 5;

    @Test
    public void generatesTilesLazily() {
        MapChunkData chunk = new MapChunkData(SEED_ONE);
        assertFalse(chunk.isGenerated());
        TileData first = chunk.getTile(0, 0);
        assertNotNull(first);
        assertSame(first, chunk.getTile(0, 0));
        for (int x = 0; x < MapChunkData.CHUNK_SIZE; x++) {
            for (int y = 0; y < MapChunkData.CHUNK_SIZE; y++) {
                chunk.getTile(x, y);
            }
        }
        assertTrue(chunk.isGenerated());
        assertEquals(MapChunkData.CHUNK_SIZE * MapChunkData.CHUNK_SIZE, chunk.getTiles().size());
    }

    @Test
    public void sameSeedProducesSameTiles() {
        MapChunkData a = new MapChunkData(SEED_TWO);
        MapChunkData b = new MapChunkData(SEED_TWO);
        assertEquals(a.getTile(SAMPLE_X, SAMPLE_Y).tileType(), b.getTile(SAMPLE_X, SAMPLE_Y).tileType());
    }
}
