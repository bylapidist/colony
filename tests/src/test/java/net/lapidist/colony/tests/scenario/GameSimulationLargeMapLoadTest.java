package net.lapidist.colony.tests.scenario;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/** Regression test ensuring larger maps load without hanging. */
@RunWith(net.lapidist.colony.tests.GdxTestRunner.class)
public class GameSimulationLargeMapLoadTest {

    @Test
    public void clientLoadsLargeMap() throws Exception {
        final int size = 60;
        try (GameServer server = new GameServer(GameServerConfig.builder()
                .width(size)
                .height(size)
                .build());
             GameClient client = new GameClient()) {
            server.start();

            CountDownLatch latch = new CountDownLatch(1);
            client.start(state -> latch.countDown());
            latch.await(2, TimeUnit.SECONDS);

            assertTrue(client.isConnected());
            MapState state = client.getMapState();
            assertNotNull(state);
        }
    }
}
