package net.lapidist.colony.tests.scenario;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.entities.BuildingComponent.BuildingType;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.components.state.BuildingData;
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

import static org.junit.Assert.assertTrue;

/** Scenario ensuring food production increments resources over time. */
@RunWith(GdxTestRunner.class)
public class GameSimulationFoodProductionTest {

    private static final int WAIT_MS = 200;
    private static final int DELAY_MS = 50;

    @Test
    public void foodIncreasesFromServerProduction() throws Exception {
        MapGenerator gen = (w, h) -> {
            MapState s = new ChunkedMapGenerator().generate(w, h);
            s.buildings().add(new BuildingData(0, 0, BuildingType.FARM.name()));
            return s.toBuilder().playerResources(new ResourceData()).build();
        };
        GameServerConfig config = GameServerConfig.builder()
                .saveName("scenario-food")
                .autosaveInterval(WAIT_MS)
                .mapGenerator(gen)
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("scenario-food");
        GameServer server = new GameServer(config);
        server.start();

        GameClient client = new GameClient();
        CountDownLatch latch = new CountDownLatch(1);
        client.start(state -> latch.countDown());
        latch.await(1, TimeUnit.SECONDS);

        MapState state = client.getMapState();
        GameSimulation sim = new GameSimulation(state, client);

        Thread.sleep(WAIT_MS + DELAY_MS);
        sim.step();

        var players = sim.getWorld().getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(PlayerResourceComponent.class))
                .getEntities();
        var prc = sim.getWorld().getMapper(PlayerResourceComponent.class)
                .get(sim.getWorld().getEntity(players.get(0)));
        assertTrue(prc.getFood() > 0);

        client.stop();
        server.stop();
        Thread.sleep(WAIT_MS);
    }
}
