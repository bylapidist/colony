package net.lapidist.colony.tests.server;

import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.server.events.TileSelectionEvent;
import net.mostlyoriginal.api.event.common.Subscribe;
import org.junit.Test;

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
        GameServer server = new GameServer();
        server.start();
        Events.getInstance().registerEvents(this);

        GameClient client = new GameClient();
        client.start();

        TileSelectionData data = new TileSelectionData();
        data.setX(0);
        data.setY(0);
        data.setSelected(true);

        client.sendTileSelection(data);
        Thread.sleep(WAIT_MS);
        Events.update();

        assertTrue(server.getMapState().getTiles().get(0).isSelected());
        assertTrue(handled);

        client.stop();
        server.stop();
    }
}
