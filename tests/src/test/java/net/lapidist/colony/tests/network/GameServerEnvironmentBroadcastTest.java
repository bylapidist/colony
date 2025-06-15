package net.lapidist.colony.tests.network;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.components.state.EnvironmentUpdate;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

public class GameServerEnvironmentBroadcastTest {
    private static final int WAIT_MS = 200;

    @Test
    public void serverBroadcastsEnvironment() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("env-broadcast")
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("env-broadcast");
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

            Thread.sleep(WAIT_MS);

            EnvironmentUpdate update = clientB.poll(EnvironmentUpdate.class);
            assertNotNull(update);
        }
    }
}
