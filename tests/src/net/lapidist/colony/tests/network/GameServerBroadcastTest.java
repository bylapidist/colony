package net.lapidist.colony.tests.network;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameServerBroadcastTest {

    private static final int WAIT_MS = 100;

    @Test
    public void serverBroadcastsTileSelection() throws Exception {
        GameServer server = new GameServer(GameServerConfig.builder().build());
        server.start();

        GameClient clientA = new GameClient();
        clientA.start();
        GameClient clientB = new GameClient();
        clientB.start();

        TileSelectionData data = new TileSelectionData(0, 0, true);
        clientA.sendTileSelection(data);

        Thread.sleep(WAIT_MS);

        TileSelectionData update = clientB.pollTileSelection();
        assertNotNull(update);
        assertEquals(data.selected(), update.selected());
        assertEquals(data.x(), update.x());
        assertEquals(data.y(), update.y());

        clientA.stop();
        clientB.stop();
        server.stop();
    }
}
