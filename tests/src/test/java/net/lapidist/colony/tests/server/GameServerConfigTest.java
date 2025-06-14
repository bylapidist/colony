package net.lapidist.colony.tests.server;

import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.map.MapGenerator;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameServerConfigTest {
    private static final long INTERVAL = 10L;

    @Test
    public void builderSetsAllValues() {
        MapGenerator gen = mock(MapGenerator.class);
        final int w = 99;
        final int h = 66;
        GameServerConfig cfg = GameServerConfig.builder()
                .saveName("save")
                .autosaveInterval(INTERVAL)
                .mapGenerator(gen)
                .width(w)
                .height(h)
                .build();
        assertEquals("save", cfg.getSaveName());
        assertEquals(INTERVAL, cfg.getAutosaveInterval());
        assertSame(gen, cfg.getMapGenerator());
        assertEquals(w, cfg.getWidth());
        assertEquals(h, cfg.getHeight());
    }
}
