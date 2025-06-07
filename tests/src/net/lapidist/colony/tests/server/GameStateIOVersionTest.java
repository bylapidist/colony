package net.lapidist.colony.tests.server;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.server.io.GameStateIO;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;

public class GameStateIOVersionTest {

    @Test
    public void throwsWhenVersionIsNewer() throws Exception {
        Path file = Files.createTempFile("state", ".dat");
        MapState state = new MapState();
        state.setVersion(MapState.CURRENT_VERSION + 1);
        GameStateIO.save(state, file);

        boolean thrown = false;
        try {
            GameStateIO.load(file);
        } catch (IOException e) {
            thrown = true;
        }
        Files.deleteIfExists(file);
        assertTrue(thrown);
    }
}
