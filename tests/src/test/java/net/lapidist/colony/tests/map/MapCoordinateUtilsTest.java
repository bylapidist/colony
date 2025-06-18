package net.lapidist.colony.tests.map;

import net.lapidist.colony.map.MapCoordinateUtils;
import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.components.state.map.ChunkPos;
import net.lapidist.colony.components.state.map.TilePos;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapCoordinateUtilsTest {
    private static final int WORLD_X = MapChunkData.CHUNK_SIZE * 2 + 5;
    private static final int WORLD_Y = MapChunkData.CHUNK_SIZE * 3 + 7;
    private static final int NEG_X = -5;
    private static final int NEG_Y = -1;
    private static final int EXPECT_CHUNK_X = 2;
    private static final int EXPECT_CHUNK_Y = 3;
    private static final int EXPECT_LOCAL_X = 5;
    private static final int EXPECT_LOCAL_Y = 7;
    private static final int EXPECT_NEG_CHUNK = -1;

    @Test
    public void convertsWorldCoordinates() {
        assertEquals(EXPECT_CHUNK_X, MapCoordinateUtils.toChunkCoord(WORLD_X));
        assertEquals(EXPECT_CHUNK_Y, MapCoordinateUtils.toChunkCoord(WORLD_Y));
        assertEquals(EXPECT_LOCAL_X, MapCoordinateUtils.toLocalCoord(WORLD_X));
        assertEquals(EXPECT_LOCAL_Y, MapCoordinateUtils.toLocalCoord(WORLD_Y));

        ChunkPos chunk = MapCoordinateUtils.toChunkPos(WORLD_X, WORLD_Y);
        TilePos local = MapCoordinateUtils.toLocalPos(WORLD_X, WORLD_Y);
        assertEquals(EXPECT_CHUNK_X, chunk.x());
        assertEquals(EXPECT_CHUNK_Y, chunk.y());
        assertEquals(EXPECT_LOCAL_X, local.x());
        assertEquals(EXPECT_LOCAL_Y, local.y());
    }

    @Test
    public void handlesNegativeCoordinates() {
        assertEquals(EXPECT_NEG_CHUNK, MapCoordinateUtils.toChunkCoord(NEG_X));
        assertEquals(EXPECT_NEG_CHUNK, MapCoordinateUtils.toChunkCoord(NEG_Y));
        assertEquals(MapChunkData.CHUNK_SIZE - EXPECT_LOCAL_X, MapCoordinateUtils.toLocalCoord(NEG_X));
        assertEquals(MapChunkData.CHUNK_SIZE - 1, MapCoordinateUtils.toLocalCoord(NEG_Y));
    }
}
