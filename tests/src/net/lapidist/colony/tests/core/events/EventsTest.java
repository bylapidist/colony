package net.lapidist.colony.tests.core.events;

import net.lapidist.colony.client.events.*;
import net.lapidist.colony.tests.GdxTestRunner;
import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.api.event.common.Subscribe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@RunWith(GdxTestRunner.class)
public class EventsTest {

    private boolean pauseHandled;
    private ResizeEvent resizeEvent;

    @Before
    public void setUp() {
        Events.init(new EventSystem());
        Events.getInstance().registerEvents(this);
    }

    @Subscribe
    private void onPause(final PauseEvent event) {
        pauseHandled = true;
    }

    @Subscribe
    private void onResize(final ResizeEvent event) {
        resizeEvent = event;
    }

    @Test
    public void testDispatch() {
        pauseHandled = false;
        Events.dispatch(new PauseEvent());
        Events.update();
        assertTrue(pauseHandled);
    }

    @Test
    public void testDispatchWithPayload() {
        final int testWidth = 1920;
        final int testHeight = 1080;
        Events.dispatch(new ResizeEvent(testWidth, testHeight));
        Events.update();
        assertEquals(testWidth, resizeEvent.getWidth());
        assertEquals(testHeight, resizeEvent.getHeight());
    }

    @Test
    public void testDispatchLogsEventString() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        PauseEvent event = new PauseEvent();
        try {
            Events.dispatch(event);
            Events.update();
        } finally {
            System.setOut(original);
        }
        String expected = String.format(
                "[%s] Dispatched event: %s%n",
                Events.class.getSimpleName(),
                event.toString()
        );
        assertEquals(expected, out.toString());
    }
}
