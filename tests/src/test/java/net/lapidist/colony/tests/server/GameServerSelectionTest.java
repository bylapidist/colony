package net.lapidist.colony.tests.server;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.server.events.TileSelectionEvent;
import net.mostlyoriginal.api.event.common.Subscribe;
import org.junit.Test;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class GameServerSelectionTest {

    private boolean handled;
    private static final int WAIT_MS = 100;

    @Subscribe
    private void onTileSelection(final TileSelectionEvent event) {
        handled = true;
    }

    @Test
    public void selectingTileUpdatesServerState() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("selection-test")
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("selection-test");
        GameServer server = new GameServer(config);
        server.start();
        Events.getInstance().registerEvents(this);

        GameClient client = new GameClient();
        CountDownLatch latch = new CountDownLatch(1);
        client.start(state -> latch.countDown());
        latch.await(1, TimeUnit.SECONDS);

        TileSelectionData data = new TileSelectionData(0, 0, true);

        client.sendTileSelectionRequest(data);
        Thread.sleep(WAIT_MS);
        Events.update();

        assertTrue(server.getMapState().getTile(0, 0).selected());
        assertTrue(handled);

        client.stop();
        server.stop();
    }
}
