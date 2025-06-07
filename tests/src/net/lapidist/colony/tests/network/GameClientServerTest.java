package net.lapidist.colony.tests.network;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.server.GameServer;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GameClientServerTest {

    @Test
    public void clientReceivesMapFromServer() throws Exception {
        GameServer server = new GameServer();
        server.start();

        GameClient client = new GameClient();
        client.start();

        assertNotNull(client.getMapState());
        assertNotNull(server.getMapState());

        client.stop();
        server.stop();
    }
}
