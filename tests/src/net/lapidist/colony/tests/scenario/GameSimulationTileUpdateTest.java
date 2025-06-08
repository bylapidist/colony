package net.lapidist.colony.tests.scenario;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class GameSimulationTileUpdateTest {

    private static final int WAIT_MS = 100;

    @Test
    public void serverUpdatesAppliedToClientWorld() throws Exception {
        GameServer server = new GameServer(GameServerConfig.builder().build());
        server.start();

        GameClient sender = new GameClient();
        sender.start();
        GameClient receiver = new GameClient();
        receiver.start();

        MapState state = receiver.getMapState();
        GameSimulation sim = new GameSimulation(state, receiver);

        TileSelectionData data = new TileSelectionData(0, 0, true);
        sender.sendTileSelection(data);

        Thread.sleep(WAIT_MS);
        sim.step();

        var world = sim.getWorld();
        var maps = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(net.lapidist.colony.components.maps.MapComponent.class))
                .getEntities();
        var map = world.getEntity(maps.get(0));
        var mapComponent = world.getMapper(net.lapidist.colony.components.maps.MapComponent.class).get(map);
        var tile = mapComponent.getTiles().get(0);
        var tileComponent = world.getMapper(net.lapidist.colony.components.maps.TileComponent.class).get(tile);
        assertTrue(tileComponent.isSelected());

        sender.stop();
        receiver.stop();
        server.stop();
    }
}
