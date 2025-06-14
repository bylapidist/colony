package net.lapidist.colony.tests.scenario;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Scenario test verifying that a client receives map data from the server and
 * can load it into a simulation world.
 */
@RunWith(GdxTestRunner.class)
public class GameSimulationMapLoadNetworkTest {

    @Test
    public void clientLoadsInitialMapFromServer() throws Exception {
        GameServer server = new GameServer(GameServerConfig.builder().build());
        server.start();

        try (GameClient client = new GameClient()) {
            CountDownLatch latch = new CountDownLatch(1);
            client.start(state -> latch.countDown());
            latch.await(1, TimeUnit.SECONDS);

        assertTrue(client.isConnected());
        MapState state = client.getMapState();
        assertNotNull(state);
        assertEquals(server.getMapState().name(), state.name());
        assertEquals(MapState.DEFAULT_WIDTH, client.getMapWidth());
        assertEquals(MapState.DEFAULT_HEIGHT, client.getMapHeight());

            GameSimulation sim = new GameSimulation(state, client);
            sim.step();

        IntBag maps = sim.getWorld().getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        assertEquals(1, maps.size());

        Entity map = sim.getWorld().getEntity(maps.get(0));
            MapComponent mapComponent = sim.getWorld().getMapper(MapComponent.class).get(map);
            assertFalse(mapComponent.getTiles().isEmpty());
        }
        server.stop();
    }
}
