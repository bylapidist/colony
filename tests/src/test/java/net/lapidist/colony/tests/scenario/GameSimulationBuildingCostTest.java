package net.lapidist.colony.tests.scenario;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.components.state.BuildingPlacementData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.map.ChunkedMapGenerator;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class GameSimulationBuildingCostTest {

    private static final int WAIT_MS = 200;

    @Test
    public void buildingConsumesPlayerResources() throws Exception {
        MapGenerator gen = (w, h) -> {
            MapState state = new ChunkedMapGenerator().generate(w, h);
            return state.toBuilder().playerResources(new ResourceData(1, 0, 0)).build();
        };
        GameServerConfig config = GameServerConfig.builder()
                .saveName("scenario-build-cost")
                .mapGenerator(gen)
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("scenario-build-cost");
        try (GameServer server = new GameServer(config);
             GameClient sender = new GameClient();
             GameClient receiver = new GameClient()) {
            server.start();

            CountDownLatch latchSender = new CountDownLatch(1);
            sender.start(state -> latchSender.countDown());
            CountDownLatch latchReceiver = new CountDownLatch(1);
            receiver.start(state -> latchReceiver.countDown());
            latchSender.await(1, TimeUnit.SECONDS);
            latchReceiver.await(1, TimeUnit.SECONDS);

        MapState state = receiver.getMapState();
        GameSimulation sim = new GameSimulation(state, receiver);

        BuildingPlacementData data = new BuildingPlacementData(0, 0, "HOUSE");
        sender.sendBuildRequest(data);

        Thread.sleep(WAIT_MS);
        sim.step();

        var players = sim.getWorld().getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(PlayerResourceComponent.class))
                .getEntities();
        var prc = sim.getWorld().getMapper(PlayerResourceComponent.class)
                .get(sim.getWorld().getEntity(players.get(0)));
        assertEquals(0, prc.getWood());

        }
        Thread.sleep(WAIT_MS);
    }

    @Test
    public void farmPlacementConsumesPlayerResources() throws Exception {
        MapGenerator gen = (w, h) -> {
            MapState state = new ChunkedMapGenerator().generate(w, h);
            return state.toBuilder().playerResources(new ResourceData(2, 0, 0)).build();
        };
        GameServerConfig config = GameServerConfig.builder()
                .saveName("scenario-farm-cost")
                .mapGenerator(gen)
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("scenario-farm-cost");
        try (GameServer server = new GameServer(config);
             GameClient sender = new GameClient();
             GameClient receiver = new GameClient()) {
            server.start();

            CountDownLatch latchSender = new CountDownLatch(1);
            sender.start(state -> latchSender.countDown());
            CountDownLatch latchReceiver = new CountDownLatch(1);
            receiver.start(state -> latchReceiver.countDown());
            latchSender.await(1, TimeUnit.SECONDS);
            latchReceiver.await(1, TimeUnit.SECONDS);

        MapState state = receiver.getMapState();
        GameSimulation sim = new GameSimulation(state, receiver);

        BuildingPlacementData data = new BuildingPlacementData(0, 0, "FARM");
        sender.sendBuildRequest(data);

        Thread.sleep(WAIT_MS);
        sim.step();

        var players = sim.getWorld().getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(PlayerResourceComponent.class))
                .getEntities();
        var prc = sim.getWorld().getMapper(PlayerResourceComponent.class)
                .get(sim.getWorld().getEntity(players.get(0)));
        assertEquals(0, prc.getWood());

        }
        Thread.sleep(WAIT_MS);
    }
}
