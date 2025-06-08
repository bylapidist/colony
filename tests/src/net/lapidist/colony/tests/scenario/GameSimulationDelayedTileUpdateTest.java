package net.lapidist.colony.tests.scenario;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
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
        Paths.deleteAutosave("scenario");
        GameServer server = new GameServer(config);
        server.start();

        GameClient client = new GameClient();
        client.start();

        try {
            MapState state = client.getMapState();
            GameSimulation sim = new GameSimulation(state, client);

            Vector2 screen = CameraUtils.worldToScreenCoords(
                    sim.getCamera().getViewport(), 0, 0
            );
            sim.getInput().tap(screen.x, screen.y, 1, 0);

            // Check immediately after tap - tile should not be selected yet
            TileComponent tile = findTile(sim);
            assertFalse(tile.isSelected());

            // Wait for server broadcast then process queued update
            Thread.sleep(WAIT_MS);
            sim.step();

            tile = findTile(sim);
            assertTrue(tile.isSelected());
        } finally {
            client.stop();
            server.stop();
        }
    }

    private TileComponent findTile(final GameSimulation sim) {
        IntBag maps = sim.getWorld().getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        Entity map = sim.getWorld().getEntity(maps.get(0));
        MapComponent mapComponent = sim.getWorld().getMapper(MapComponent.class)
                .get(map);
        for (int i = 0; i < mapComponent.getTiles().size; i++) {
            TileComponent tc = sim.getWorld()
                    .getMapper(TileComponent.class)
                    .get(mapComponent.getTiles().get(i));
            if (tc.getX() == 0 && tc.getY() == 0) {
                return tc;
            }
        }
        return null;
    }
}
