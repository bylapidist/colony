package net.lapidist.colony.tests.scenario;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.components.state.messages.BuildingPlacementData;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.resources.ResourceData;
import net.lapidist.colony.map.ChunkedMapGenerator;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.mod.ModLoader;
import net.lapidist.colony.mod.ModLoader.LoadedMod;
import net.lapidist.colony.mod.ModMetadata;
import net.lapidist.colony.mod.test.BuildingCostMod;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

/** Scenario verifying mods can register new buildings with custom costs. */
@RunWith(GdxTestRunner.class)
public class GameSimulationCustomBuildingCostTest {

    private static final int WAIT_MS = 200;
    private static final int INITIAL_WOOD = 3;

    @Test
    public void customBuildingConsumesResources() throws Exception {
        MapGenerator gen = (w, h) -> {
            MapState state = new ChunkedMapGenerator().generate(w, h);
            return state.toBuilder().playerResources(new ResourceData(INITIAL_WOOD, 0, 0)).build();
        };
        GameServerConfig config = GameServerConfig.builder()
                .saveName("scenario-custom-cost")
                .mapGenerator(gen)
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("scenario-custom-cost");
        try (MockedConstruction<ModLoader> loader = mockConstruction(ModLoader.class,
                (m, c) -> when(m.loadMods()).thenReturn(List.of(
                        new LoadedMod(new BuildingCostMod(), new ModMetadata("cost", "1", List.of()), null))));
             GameServer server = new GameServer(config);
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

            BuildingPlacementData data = new BuildingPlacementData(0, 0, "hut");
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
