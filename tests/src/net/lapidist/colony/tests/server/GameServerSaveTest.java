package net.lapidist.colony.tests.server;

import net.lapidist.colony.server.GameServer;
import net.lapidist.colony.server.events.AutosaveEvent;
import net.lapidist.colony.server.events.Events;
import net.lapidist.colony.server.io.GameStateIO;
import net.lapidist.colony.server.io.Paths;
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

    @Before
    public void setUp() throws IOException {
        Path saveFile = Paths.getSaveFile("autosave.dat");
        Files.deleteIfExists(saveFile);
        Paths.createGameFoldersIfNotExists();
    }

    @Subscribe
    private void onAutosave(final AutosaveEvent event) {
        lastEvent = event;
    }

    @Test
    public void autosaveCreatesSaveFileAndFiresEvent() throws Exception {
        GameServer server = new GameServer(TEST_INTERVAL_MS);
        server.start();
        Events.getInstance().registerEvents(this);

        Thread.sleep(WAIT_MS);
        Events.update();

        Path saveFile = Paths.getSaveFile("autosave.dat");
        assertTrue(Files.exists(saveFile));
        assertTrue(lastEvent != null);
        assertTrue(saveFile.equals(lastEvent.getLocation()));
        assertTrue(lastEvent.getSize() > 0);

        server.stop();
    }

    @Test
    public void loadsExistingSave() throws Exception {
        GameServer first = new GameServer(TEST_INTERVAL_MS);
        first.start();
        first.getMapState().getTiles().get(0).setTextureRef("changed");
        GameStateIO.save(first.getMapState(), Paths.getSaveFile("autosave.dat"));
        first.stop();

        GameServer second = new GameServer(TEST_INTERVAL_MS);
        second.start();
        String loaded = second.getMapState().getTiles().get(0).getTextureRef();
        second.stop();

        assertTrue("changed".equals(loaded));
    }
}
