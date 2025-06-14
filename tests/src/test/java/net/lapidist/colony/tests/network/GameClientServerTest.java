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
        try (GameServer server = new GameServer(GameServerConfig.builder().build());
             GameClient client = new GameClient()) {
            server.start();

            CountDownLatch latch = new CountDownLatch(1);
            client.start(state -> latch.countDown());
            latch.await(1, TimeUnit.SECONDS);

            assertNotNull(client.getMapState());
            assertNotNull(server.getMapState());
        }
    }

    @Test
    public void clientReceivesMapUsingDefaultStart() throws Exception {
        try (GameServer server = new GameServer(GameServerConfig.builder().build());
             GameClient client = new GameClient()) {
            server.start();

            client.start();

        final int maxAttempts = 20;
        final int delayMs = 50;
        int attempts = 0;
        while (client.getMapState() == null && attempts < maxAttempts) {
            Thread.sleep(delayMs);
            attempts++;
        }

        assertNotNull(client.getMapState());
        }
    }
}
