package net.lapidist.colony.tests.server;

import net.lapidist.colony.io.Paths;
import net.lapidist.colony.io.TestPathService;
import net.lapidist.colony.map.ChunkedMapGenerator;
import net.lapidist.colony.server.services.MapService;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertTrue;

public class MapServiceConfigTest {

    private static final int SIZE = 8;

    @Test
    public void loadWritesConfigFile() throws Exception {
        Path temp = Files.createTempDirectory("mapservice-config");
        Paths paths = new Paths(new TestPathService(temp));
        Path config = paths.getConfigFile();

        try (MockedStatic<Paths> mock = Mockito.mockStatic(Paths.class)) {
            mock.when(Paths::get).thenReturn(paths);
            MapService service = new MapService(
                    new ChunkedMapGenerator(),
                    "cfg-test",
                    SIZE,
                    SIZE,
                    new ReentrantLock()
            );

            service.load();
        }

        assertTrue(Files.exists(config));
        java.util.List<String> lines = Files.readAllLines(config);
        assertTrue(lines.contains("cfg-test"));
    }
}
