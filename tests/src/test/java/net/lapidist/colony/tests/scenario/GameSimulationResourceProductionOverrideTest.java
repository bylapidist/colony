package net.lapidist.colony.tests.scenario;

import com.artemis.Aspect;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.map.ChunkedMapGenerator;
import net.lapidist.colony.map.MapGenerator;
import net.lapidist.colony.mod.ModLoader;
import net.lapidist.colony.mod.ModLoader.LoadedMod;
import net.lapidist.colony.mod.ModMetadata;
import net.lapidist.colony.mod.test.ResourceStubMod;
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

/** Scenario verifying a mod can override resource production service. */
@RunWith(GdxTestRunner.class)
public class GameSimulationResourceProductionOverrideTest {

    private static final int WAIT_MS = 100;
    private static final int DELAY_MS = 50;

    @Test
    public void overriddenServicePreventsFoodProduction() throws Exception {
        MapGenerator gen = (w, h) -> {
            MapState s = new ChunkedMapGenerator().generate(w, h);
            s.buildings().add(new BuildingData(0, 0, "farm"));
            return s.toBuilder().playerResources(new ResourceData()).build();
        };
        GameServerConfig config = GameServerConfig.builder()
                .saveName("scenario-mod-prod")
                .autosaveInterval(WAIT_MS)
                .mapGenerator(gen)
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("scenario-mod-prod");
        ResourceStubMod.START_CALLS.set(0);
        try (MockedConstruction<ModLoader> loader = mockConstruction(ModLoader.class,
                (m, c) -> when(m.loadMods()).thenReturn(List.of(
                        new LoadedMod(new ResourceStubMod(), new ModMetadata("stub", "1", List.of()), null))));
             GameServer server = new GameServer(config);
             GameClient client = new GameClient()) {
            server.start();
            assertEquals(1, ResourceStubMod.START_CALLS.get());

            CountDownLatch latch = new CountDownLatch(1);
            client.start(state -> latch.countDown());
            latch.await(1, TimeUnit.SECONDS);

            MapState state = client.getMapState();
            GameSimulation sim = new GameSimulation(state, client);

            Thread.sleep(WAIT_MS + DELAY_MS);
            sim.step();

            var players = sim.getWorld().getAspectSubscriptionManager()
                    .get(Aspect.all(PlayerResourceComponent.class))
                    .getEntities();
            var prc = sim.getWorld().getMapper(PlayerResourceComponent.class)
                    .get(sim.getWorld().getEntity(players.get(0)));
            assertEquals(0, prc.getFood());
        }
        Thread.sleep(WAIT_MS);
    }
}
