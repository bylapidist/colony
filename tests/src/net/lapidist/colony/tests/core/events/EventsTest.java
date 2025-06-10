package net.lapidist.colony.tests.core.events;

import net.lapidist.colony.client.events.*;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.tests.GdxTestRunner;
import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.api.event.common.Subscribe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.List;
import org.slf4j.LoggerFactory;

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
        assertEquals(testWidth, resizeEvent.width());
        assertEquals(testHeight, resizeEvent.height());
    }

    @Test
    public void testDispatchLogsEventString() {
        Logger logger = (Logger) LoggerFactory.getLogger(Events.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);

        PauseEvent event = new PauseEvent();
        Events.dispatch(event);
        Events.update();

        logger.detachAppender(appender);
        List<ILoggingEvent> logs = appender.list;
        assertEquals(1, logs.size());
        assertEquals("Dispatched event: " + event.toString(), logs.get(0).getFormattedMessage());
    }

    @Test
    public void testPauseEventToString() {
        assertEquals("PauseEvent", new PauseEvent().toString());
    }

    @Test
    public void testResizeEventToString() {
        final int width = 800;
        final int height = 600;
        ResizeEvent event = new ResizeEvent(width, height);
        assertEquals(
                String.format("ResizeEvent(width=%d, height=%d)", width, height),
                event.toString()
        );
    }
}
