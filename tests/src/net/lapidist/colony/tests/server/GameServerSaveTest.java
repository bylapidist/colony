package net.lapidist.colony.tests.server;

import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.server.events.AutosaveEvent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.server.events.ShutdownSaveEvent;
import net.lapidist.colony.server.io.GameStateIO;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.mostlyoriginal.api.event.common.Subscribe;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;

public class GameServerSaveTest {

    private static final int TEST_INTERVAL_MS = 100;
    private static final int WAIT_MS = 500;
    private AutosaveEvent lastEvent;
    private ShutdownSaveEvent shutdownEvent;

    @Before
    public void setUp() throws IOException {
        Path saveFile = Paths.get().getAutosave("autosave");
        Files.deleteIfExists(saveFile);
        Paths.get().createGameFoldersIfNotExists();
    }

    @Subscribe
    private void onAutosave(final AutosaveEvent event) {
        lastEvent = event;
    }

    @Subscribe
    private void onShutdownSave(final ShutdownSaveEvent event) {
        shutdownEvent = event;
    }

    @Test
    public void autosaveCreatesSaveFileAndFiresEvent() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("save-test")
                .autosaveInterval(TEST_INTERVAL_MS)
                .build();
        net.lapidist.colony.io.Paths.get().deleteAutosave("save-test");
        GameServer server = new GameServer(config);
        server.start();
        Events.getInstance().registerEvents(this);

        Thread.sleep(WAIT_MS);
        Events.update();

        Path saveFile = Paths.get().getAutosave("save-test");
        assertTrue(Files.exists(saveFile));
        assertTrue(lastEvent != null);
        assertTrue(saveFile.equals(lastEvent.getLocation()));
        assertTrue(lastEvent.getSize() > 0);

        server.stop();
    }

    @Test
    public void stoppingSavesFileAndFiresEvent() throws Exception {
        GameServerConfig config = GameServerConfig.builder()
                .saveName("save-test")
                .autosaveInterval(TEST_INTERVAL_MS)
                .build();
        GameServer server = new GameServer(config);
        server.start();
        Events.getInstance().registerEvents(this);

        server.stop();

        Path saveFile = Paths.get().getAutosave("save-test");
        assertTrue(Files.exists(saveFile));
        assertTrue(shutdownEvent != null);
        assertTrue(saveFile.equals(shutdownEvent.getLocation()));
        assertTrue(shutdownEvent.getSize() > 0);
    }

    @Test
    public void loadsExistingSave() throws Exception {
        GameServerConfig cfg = GameServerConfig.builder()
                .saveName("save-test")
                .autosaveInterval(TEST_INTERVAL_MS)
                .build();
        GameServer first = new GameServer(cfg);
        first.start();
        TilePos pos = new TilePos(0, 0);
        TileData modified = first.getMapState().tiles().get(pos)
                .toBuilder()
                .textureRef("changed")
                .build();
        first.getMapState().tiles().put(pos, modified);
        GameStateIO.save(first.getMapState(), Paths.get().getAutosave("save-test"));
        first.stop();

        GameServer second = new GameServer(cfg);
        second.start();
        String loaded = second.getMapState().tiles().get(new TilePos(0, 0)).textureRef();
        second.stop();

        assertTrue("changed".equals(loaded));
    }
}
