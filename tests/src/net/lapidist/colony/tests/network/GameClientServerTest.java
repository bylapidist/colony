package net.lapidist.colony.tests.network;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import org.junit.Test;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

public class GameClientServerTest {

    @Test
    public void clientReceivesMapFromServer() throws Exception {
        GameServer server = new GameServer(GameServerConfig.builder().build());
        server.start();

        GameClient client = new GameClient();
        CountDownLatch latch = new CountDownLatch(1);
        client.start(state -> latch.countDown());
        latch.await(1, TimeUnit.SECONDS);

        assertNotNull(client.getMapState());
        assertNotNull(server.getMapState());

        client.stop();
        server.stop();
    }
}
