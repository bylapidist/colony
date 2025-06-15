package net.lapidist.colony.tests.server;

import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.server.services.ResourceProductionService;
import net.lapidist.colony.io.Paths;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.assertTrue;

/** Ensures built-in mods register default game systems. */
public class GameServerBaseSystemsTest {

    @SuppressWarnings("unchecked")
    private static List<Object> systems(final GameServer server) throws Exception {
        Field f = GameServer.class.getDeclaredField("systems");
        f.setAccessible(true);
        return (List<Object>) f.get(server);
    }

    @Test
    public void resourceProductionSystemRegistered() throws Exception {
        String name = "base-systems";
        Paths.get().deleteAutosave(name);
        GameServer server = new GameServer(GameServerConfig.builder().saveName(name).build());
        server.start();

        assertTrue(systems(server).stream().anyMatch(s -> s instanceof ResourceProductionService));
        server.stop();
    }
}
