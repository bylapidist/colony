package net.lapidist.colony.tests.server.events;

import net.lapidist.colony.server.events.AutosaveEvent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.server.events.ShutdownSaveEvent;
import net.lapidist.colony.server.events.TileSelectionEvent;
import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.api.event.common.Subscribe;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServerEventsTest {

    private static final long TEST_SIZE = 123L;
    private boolean handled;
    private AutosaveEvent received;
    private ShutdownSaveEvent shutdownReceived;

    @Before
    public void setUp() {
        Events.init(new EventSystem());
        Events.getInstance().registerEvents(this);
    }

    @Subscribe
    private void onAutosave(final AutosaveEvent event) {
        handled = true;
        received = event;
    }

    @Subscribe
    private void onShutdownSave(final ShutdownSaveEvent event) {
        handled = true;
        shutdownReceived = event;
    }

    @Test
    public void dispatchAutosaveEvent() {
        handled = false;
        Path file = Paths.get("dummy.dat");
        Events.dispatch(new AutosaveEvent(file, TEST_SIZE));
        Events.update();
        assertTrue(handled);
        assertEquals(file, received.getLocation());
        assertEquals(TEST_SIZE, received.getSize());
    }

    @Test
    public void dispatchShutdownSaveEvent() {
        handled = false;
        Path file = Paths.get("shutdown.dat");
        Events.dispatch(new ShutdownSaveEvent(file, TEST_SIZE));
        Events.update();
        assertTrue(handled);
        assertEquals(file, shutdownReceived.getLocation());
        assertEquals(TEST_SIZE, shutdownReceived.getSize());
    }

    @Test
    public void testAutosaveEventToString() {
        Path file = Paths.get("autosave.dat");
        AutosaveEvent event = new AutosaveEvent(file, TEST_SIZE);
        String expected = String.format(
                "AutosaveEvent(location=%s, size=%d)",
                file,
                TEST_SIZE
        );
        assertEquals(expected, event.toString());
    }

    @Test
    public void testShutdownSaveEventToString() {
        Path file = Paths.get("autosave.dat");
        ShutdownSaveEvent event = new ShutdownSaveEvent(file, TEST_SIZE);
        String expected = String.format(
                "ShutdownSaveEvent(location=%s, size=%d)",
                file,
                TEST_SIZE
        );
        assertEquals(expected, event.toString());
    }

    @Test
    public void testTileSelectionEventToString() {
        final int x = 1;
        final int y = 2;
        TileSelectionEvent event = new TileSelectionEvent(x, y, true);
        assertEquals(
                String.format("TileSelectionEvent(x=%d, y=%d, selected=true)", x, y),
                event.toString()
        );
    }
}
