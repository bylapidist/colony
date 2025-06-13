package net.lapidist.colony.tests.scenario;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceGatherRequestData;
import net.lapidist.colony.components.resources.ResourceComponent;
import net.lapidist.colony.components.resources.ResourceType;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class GameSimulationResourceUpdateTest {

    private static final int WAIT_MS = 100;
    private static final int MAX_WOOD = 10;

    @Test
    public void serverUpdatesAppliedToClientWorld() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("scenario-gather")
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("scenario-gather");
        GameServer server = new GameServer(config);
        server.start();

        GameClient sender = new GameClient();
        CountDownLatch latchSender = new CountDownLatch(1);
        sender.start(state -> latchSender.countDown());
        GameClient receiver = new GameClient();
        CountDownLatch latchReceiver = new CountDownLatch(1);
        receiver.start(state -> latchReceiver.countDown());
        latchSender.await(1, TimeUnit.SECONDS);
        latchReceiver.await(1, TimeUnit.SECONDS);

        MapState state = receiver.getMapState();
        GameSimulation sim = new GameSimulation(state, receiver);

        ResourceGatherRequestData data = new ResourceGatherRequestData(0, 0, ResourceType.WOOD);
        sender.sendGatherRequest(data);

        Thread.sleep(WAIT_MS);
        sim.step();

        var world = sim.getWorld();
        var maps = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(net.lapidist.colony.components.maps.MapComponent.class))
                .getEntities();
        var map = world.getEntity(maps.get(0));
        var mapComponent = world.getMapper(net.lapidist.colony.components.maps.MapComponent.class).get(map);
        var tile = mapComponent.getTiles().get(0);
        var rc = world.getMapper(ResourceComponent.class).get(tile);
        assertTrue(rc.getWood() <= MAX_WOOD);

        sender.stop();
        receiver.stop();
        server.stop();
    }
}
