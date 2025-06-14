package net.lapidist.colony.tests.server;

import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.components.state.PlayerPosition;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameServerPlayerPositionSaveTest {

    @Test
    public void playerPositionPersistsAcrossSaves() throws Exception {
        final long waitMs = 200;
        final long extra = 50;
        GameServerConfig config = GameServerConfig.builder()
                .saveName("pos-save")
                .autosaveInterval(waitMs)
                .build();
        Paths.get().deleteAutosave("pos-save");
        GameServer server = new GameServer(config);
        server.start();

        java.lang.reflect.Method dispatch = net.lapidist.colony.network.AbstractMessageEndpoint.class
                .getDeclaredMethod("dispatch", Object.class);
        dispatch.setAccessible(true);

        final int x = 3;
        final int y = 4;
        dispatch.invoke(server, new net.lapidist.colony.components.state.PlayerPositionUpdate(x, y));
        Thread.sleep(waitMs + extra);
        server.stop();

        GameServer server2 = new GameServer(config);
        server2.start();
        PlayerPosition loaded = server2.getMapState().playerPos();
        server2.stop();

        assertEquals(x, loaded.x());
        assertEquals(y, loaded.y());
    }
}
