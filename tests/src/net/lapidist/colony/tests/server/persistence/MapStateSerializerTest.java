package net.lapidist.colony.tests.server.persistence;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.server.persistence.JavaGameStateSerializer;
import net.lapidist.colony.server.persistence.MapGenerator;
import net.lapidist.colony.server.persistence.MapStateManager;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapStateSerializerTest {

    @Test
    public void testSaveAndLoad() throws Exception {
        File tempDir = Files.createTempDirectory("colony-test").toFile();
        File saveFile = new File(tempDir, "map.dat");
        MapStateManager manager = new MapStateManager(new JavaGameStateSerializer<>(), saveFile);

        MapState generated = MapGenerator.generate();
        new JavaGameStateSerializer<MapState>().serialize(generated, saveFile);
        MapState loaded = manager.loadOrCreate();

        assertNotNull(loaded);
        assertEquals(generated.getTiles().size(), loaded.getTiles().size());
        assertEquals(generated.getBuildings().size(), loaded.getBuildings().size());
    }
}
