package net.lapidist.colony.tests.server.events;

import net.lapidist.colony.server.events.AutosaveEvent;
import net.lapidist.colony.server.events.Events;
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
}
