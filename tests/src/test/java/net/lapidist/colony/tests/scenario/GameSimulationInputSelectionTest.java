package net.lapidist.colony.tests.scenario;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.map.MapUtils;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Scenario test verifying that tapping a tile sends a network update which is
 * applied back to the client world.
 */
@RunWith(GdxTestRunner.class)
public class GameSimulationInputSelectionTest {

    private static final int WAIT_MS = 200;

    @Test
    public void tapSelectsTileViaNetwork() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("scenario")
                .build();
        Paths.get().deleteAutosave("scenario");
        GameServer server = new GameServer(config);
        server.start();

        try (GameClient client = new GameClient()) {
            CountDownLatch latch = new CountDownLatch(1);
            client.start(state -> latch.countDown());
            latch.await(1, TimeUnit.SECONDS);

            MapState state = client.getMapState();
            GameSimulation sim = new GameSimulation(state, client);

            // Convert tile (0,0) world coordinates to screen coordinates
            Vector2 screen = CameraUtils.worldToScreenCoords(
                    sim.getCamera().getViewport(), 0, 0
            );
            sim.getSelection().tap(screen.x, screen.y);

            Thread.sleep(WAIT_MS);
            sim.step();

            IntBag maps = sim.getWorld().getAspectSubscriptionManager()
                    .get(Aspect.all(MapComponent.class))
                    .getEntities();
            Entity map = sim.getWorld().getEntity(maps.get(0));
            MapComponent mapComponent =
                    sim.getWorld().getMapper(MapComponent.class).get(map);
            var tileOpt = MapUtils.findTile(
                    mapComponent,
                    0,
                    0
            );
            TileComponent tile = tileOpt.map(
                    t -> sim.getWorld().getMapper(TileComponent.class).get(t)
            ).orElse(null);
            assertTrue(tile != null && tile.isSelected());
        }
        finally {
            server.stop();
        }
    }
}
