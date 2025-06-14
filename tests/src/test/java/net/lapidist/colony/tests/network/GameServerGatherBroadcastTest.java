package net.lapidist.colony.tests.network;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.ResourceGatherRequestData;
import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.components.resources.ResourceType;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameServerGatherBroadcastTest {

    private static final int WAIT_MS = 100;

    @Test
    public void serverBroadcastsResourceGather() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("gather-broadcast")
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("gather-broadcast");
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

        ResourceGatherRequestData data = new ResourceGatherRequestData(0, 0, ResourceType.WOOD);
        clientA.sendGatherRequest(data);

        Thread.sleep(WAIT_MS);

        ResourceUpdateData update = clientB.poll(ResourceUpdateData.class);
        assertNotNull(update);
        assertEquals(0, update.x());
        assertEquals(0, update.y());

        }
    }
}
