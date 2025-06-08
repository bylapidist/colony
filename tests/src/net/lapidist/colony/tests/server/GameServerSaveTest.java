package net.lapidist.colony.tests.server;

import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.GameServerConfig;
import net.lapidist.colony.server.events.AutosaveEvent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.server.events.ShutdownSaveEvent;
import net.lapidist.colony.server.io.GameStateIO;
import net.lapidist.colony.io.Paths;
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
        Path saveFile = Paths.getAutosave("autosave");
        Files.deleteIfExists(saveFile);
        Paths.createGameFoldersIfNotExists();
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
        GameServer server = new GameServer(
                GameServerConfig.builder().autosaveIntervalMs(TEST_INTERVAL_MS).build()
        );
        server.start();
        Events.getInstance().registerEvents(this);

        Thread.sleep(WAIT_MS);
        Events.update();

        Path saveFile = Paths.getAutosave("autosave");
        assertTrue(Files.exists(saveFile));
        assertTrue(lastEvent != null);
        assertTrue(saveFile.equals(lastEvent.getLocation()));
        assertTrue(lastEvent.getSize() > 0);

        server.stop();
    }

    @Test
    public void stoppingSavesFileAndFiresEvent() throws Exception {
        GameServer server = new GameServer(
                GameServerConfig.builder().autosaveIntervalMs(TEST_INTERVAL_MS).build()
        );
        server.start();
        Events.getInstance().registerEvents(this);

        server.stop();

        Path saveFile = Paths.getAutosave("autosave");
        assertTrue(Files.exists(saveFile));
        assertTrue(shutdownEvent != null);
        assertTrue(saveFile.equals(shutdownEvent.getLocation()));
        assertTrue(shutdownEvent.getSize() > 0);
    }

    @Test
    public void loadsExistingSave() throws Exception {
        GameServer first = new GameServer(
                GameServerConfig.builder().autosaveIntervalMs(TEST_INTERVAL_MS).build()
        );
        first.start();
        first.getMapState().tiles().get(new TilePos(0, 0)).setTextureRef("changed");
        GameStateIO.save(first.getMapState(), Paths.getAutosave("autosave"));
        first.stop();

        GameServer second = new GameServer(
                GameServerConfig.builder().autosaveIntervalMs(TEST_INTERVAL_MS).build()
        );
        second.start();
        String loaded = second.getMapState().tiles().get(new TilePos(0, 0)).getTextureRef();
        second.stop();

        assertTrue("changed".equals(loaded));
    }
}
