package net.lapidist.colony.tests.scenario;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceGatherRequestData;
import net.lapidist.colony.components.resources.ResourceType;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class GameSimulationPlayerResourcesTest {

    private static final int WAIT_MS = 100;

    @Test
    public void gatheringIncreasesPlayerInventory() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("scenario-player")
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("scenario-player");
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

        ResourceGatherRequestData data = new ResourceGatherRequestData(
                0,
                0,
                ResourceType.WOOD
        );
        sender.sendGatherRequest(data);

        Thread.sleep(WAIT_MS);
        sim.step();

        var world = sim.getWorld();
        var players = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(PlayerResourceComponent.class))
                .getEntities();
        var prc = world.getMapper(PlayerResourceComponent.class).get(world.getEntity(players.get(0)));
        assertTrue(prc.getWood() > 0);

        sender.stop();
        receiver.stop();
        server.stop();
    }
}
