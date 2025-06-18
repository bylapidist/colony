package net.lapidist.colony.tests.network;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.map.BuildingData;
import net.lapidist.colony.components.state.messages.BuildingPlacementData;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.map.ChunkedMapGenerator;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.resources.ResourceData;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.components.state.resources.ResourceUpdateData;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameServerBuildBroadcastTest {

    private static final int WAIT_MS = 200;

    @Test
    public void serverBroadcastsBuildingPlacement() throws Exception {
        MapGenerator gen = (w, h) -> {
            MapState state = new ChunkedMapGenerator().generate(w, h);
            return state.toBuilder().playerResources(new ResourceData(1, 0, 0)).build();
        };
        GameServerConfig config = GameServerConfig.builder()
                .saveName("build-broadcast")
                .mapGenerator(gen)
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("build-broadcast");
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

        BuildingPlacementData data = new BuildingPlacementData(0, 0, "house");
        clientA.sendBuildRequest(data);

        Thread.sleep(WAIT_MS);

        BuildingData update = clientB.poll(BuildingData.class);
        assertNotNull(update);
        assertEquals(data.x(), update.x());
        assertEquals(data.y(), update.y());
        ResourceUpdateData res = clientB.poll(ResourceUpdateData.class);
        assertNotNull(res);
        assertEquals(0, res.amounts().getOrDefault("WOOD", 0).intValue());

        }
        Thread.sleep(WAIT_MS);
    }

    @Test
    public void serverBroadcastsFarmPlacement() throws Exception {
        MapGenerator gen = (w, h) -> {
            MapState state = new ChunkedMapGenerator().generate(w, h);
            return state.toBuilder().playerResources(new ResourceData(2, 0, 0)).build();
        };
        GameServerConfig config = GameServerConfig.builder()
                .saveName("farm-broadcast")
                .mapGenerator(gen)
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("farm-broadcast");
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

        BuildingPlacementData data = new BuildingPlacementData(0, 0, "farm");
        clientA.sendBuildRequest(data);

        Thread.sleep(WAIT_MS);

        BuildingData update = clientB.poll(BuildingData.class);
        assertNotNull(update);
        assertEquals(data.x(), update.x());
        assertEquals(data.y(), update.y());
        ResourceUpdateData res = clientB.poll(ResourceUpdateData.class);
        assertNotNull(res);
        assertEquals(0, res.amounts().getOrDefault("WOOD", 0).intValue());

        }
        Thread.sleep(WAIT_MS);
    }
}
