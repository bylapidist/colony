package net.lapidist.colony.tests.server;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.BuildingPlacementData;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.server.events.BuildingPlacedEvent;
import net.mostlyoriginal.api.event.common.Subscribe;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class GameServerBuildTest {

    private boolean handled;
    private static final int WAIT_MS = 200;

    @Subscribe
    private void onBuildingPlaced(final BuildingPlacedEvent event) {
        handled = true;
    }

    @Test
    public void buildingPlacementUpdatesServerState() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("build-test")
                .build();
        net.lapidist.colony.io.Paths.deleteAutosave("build-test");
        GameServer server = new GameServer(config);
        server.start();
        Events.getInstance().registerEvents(this);

        GameClient client = new GameClient();
        CountDownLatch latch = new CountDownLatch(1);
        client.start(state -> latch.countDown());
        latch.await(1, TimeUnit.SECONDS);

        BuildingPlacementData data = new BuildingPlacementData(0, 0, "HOUSE");
        client.sendBuildRequest(data);
        Thread.sleep(WAIT_MS);
        Events.update();

        assertTrue(server.getMapState().buildings().stream()
                .anyMatch(b -> b.x() == 0 && b.y() == 0));
        assertTrue(handled);

        client.stop();
        server.stop();
        Thread.sleep(WAIT_MS);
    }
}
