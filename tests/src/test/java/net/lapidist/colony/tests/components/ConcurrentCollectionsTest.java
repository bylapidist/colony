package net.lapidist.colony.tests.components;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.map.MapChunkData;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertTrue;

public class ConcurrentCollectionsTest {
    @Test
    public void usesConcurrentMaps() {
        MapState state = new MapState();
        assertTrue(state.chunks() instanceof ConcurrentHashMap);
        MapChunkData chunk = new MapChunkData();
        assertTrue(chunk.getTiles() instanceof ConcurrentHashMap);
    }
}
