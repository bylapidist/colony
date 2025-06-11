package net.lapidist.colony.tests.server;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.ResourceGatherRequestData;
import net.lapidist.colony.components.resources.ResourceType;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.io.Paths;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class GameServerPlayerResourceSaveTest {

    private static final int WAIT_MS = 200;

    @Test
    public void playerResourcesPersistAcrossSaves() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("resource-save")
                .autosaveInterval(WAIT_MS)
                .build();
        Paths.get().deleteAutosave("resource-save");
        GameServer server = new GameServer(config);
        server.start();

        GameClient client = new GameClient();
        CountDownLatch latch = new CountDownLatch(1);
        client.start(state -> latch.countDown());
        latch.await(1, TimeUnit.SECONDS);

        client.sendGatherRequest(new ResourceGatherRequestData(0, 0, ResourceType.WOOD));
        Thread.sleep(WAIT_MS);

        client.stop();
        server.stop();

        GameServer server2 = new GameServer(config);
        server2.start();
        int wood = server2.getMapState().playerResources().wood();
        server2.stop();

        assertTrue(wood > 0);
    }
}
