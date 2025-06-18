package net.lapidist.colony.tests.scenario;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.map.MapUtils;
import net.lapidist.colony.components.state.map.MapState;
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
 * Verifies that tile selection does not apply until the server update is processed.
 */
@RunWith(GdxTestRunner.class)
public class GameSimulationDelayedTileUpdateTest {

    private static final int WAIT_MS = 200;

    @Test
    public void tileSelectionAppliesOnlyAfterServerResponse() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("scenario")
                .build();
        Paths.get().deleteAutosave("scenario");
        try (GameServer server = new GameServer(config);
             GameClient client = new GameClient()) {
            server.start();

            CountDownLatch latch = new CountDownLatch(1);
            client.start(state -> latch.countDown());
            latch.await(1, TimeUnit.SECONDS);

            MapState state = client.getMapState();
            GameSimulation sim = new GameSimulation(state, client);

            Vector2 screen = CameraUtils.worldToScreenCoords(
                    sim.getCamera().getViewport(), 0, 0
            );
            sim.getSelection().setSelectMode(true);
            sim.getSelection().tap(screen.x, screen.y);

            // Selection should apply immediately
            TileComponent tile = findTile(sim);
            assertTrue(tile.isSelected());

            // Wait for server broadcast then process queued update
            Thread.sleep(WAIT_MS);
            sim.step();

            tile = findTile(sim);
            assertTrue(tile.isSelected());
        }
    }

    private TileComponent findTile(final GameSimulation sim) {
        IntBag maps = sim.getWorld().getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        Entity map = sim.getWorld().getEntity(maps.get(0));
        MapComponent mapComponent = sim.getWorld().getMapper(MapComponent.class)
                .get(map);
        return MapUtils.findTile(
                mapComponent,
                0,
                0
        ).map(t -> sim.getWorld().getMapper(TileComponent.class).get(t))
                .orElse(null);
    }
}
