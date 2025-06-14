package net.lapidist.colony.tests.scenario;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.BuildingRemovalData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.map.ChunkedMapGenerator;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;

@RunWith(GdxTestRunner.class)
public class GameSimulationBuildingRemovalTest {

    private static final int WAIT_MS = 200;

    @Test
    public void buildingRemovalIsBroadcastAndApplied() throws Exception {
        MapGenerator gen = (w, h) -> {
            MapState state = new ChunkedMapGenerator().generate(w, h);
            state.buildings().clear();
            state.buildings().add(new BuildingData(0, 0, "house"));
            return state;
        };
        GameServerConfig config = GameServerConfig.builder()
                .saveName("scenario-remove")
                .mapGenerator(gen)
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("scenario-remove");
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

        sender.sendRemoveBuildingRequest(new BuildingRemovalData(0, 0));

        Thread.sleep(WAIT_MS);
        sim.step();

        var world = sim.getWorld();
        var maps = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(net.lapidist.colony.components.maps.MapComponent.class))
                .getEntities();
        var map = world.getEntity(maps.get(0));
        var mapComponent = world.getMapper(net.lapidist.colony.components.maps.MapComponent.class).get(map);
        boolean found = false;
        for (int i = 0; i < mapComponent.getEntities().size; i++) {
            var building = mapComponent.getEntities().get(i);
            var bc = world.getMapper(BuildingComponent.class).get(building);
            if (bc.getX() == 0 && bc.getY() == 0) {
                found = true;
                break;
            }
        }
        assertFalse(found);

        }
        Thread.sleep(WAIT_MS);
    }
}
