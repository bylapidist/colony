package net.lapidist.colony.tests.network;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import org.junit.Test;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameServerBroadcastTest {

    private static final int WAIT_MS = 100;

    @Test
    public void serverBroadcastsTileSelection() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("broadcast")
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("broadcast");
        try (GameServer server = new GameServer(config);
             GameClient clientA = new GameClient();
             GameClient clientB = new GameClient()) {
            server.start();

            CountDownLatch latchA = new CountDownLatch(1);
            clientA.start(state -> latchA.countDown());
            CountDownLatch latchB = new CountDownLatch(1);
            clientB.start(state -> latchB.countDown());
            latchA.await(1, TimeUnit.SECONDS);
            latchB.await(1, TimeUnit.SECONDS);

        TileSelectionData data = new TileSelectionData(0, 0, true);
        clientA.sendTileSelectionRequest(data);

        Thread.sleep(WAIT_MS);

        TileSelectionData update = clientB.poll(TileSelectionData.class);
        assertNotNull(update);
        assertEquals(data.selected(), update.selected());
        assertEquals(data.x(), update.x());
        assertEquals(data.y(), update.y());

        }
    }
}
