package net.lapidist.colony.tests.scenario;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.messages.TileSelectionData;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class GameSimulationTileUpdateTest {

    private static final int WAIT_MS = 100;

    @Test
    public void serverUpdatesAppliedToClientWorld() throws Exception {
        try (GameServer server = new GameServer(GameServerConfig.builder().build());
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

        TileSelectionData data = new TileSelectionData(0, 0, true);
        sender.sendTileSelectionRequest(data);

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

        }
    }
}
