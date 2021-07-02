package net.lapidist.colony.tests.core.events;

import net.lapidist.colony.core.events.EventType;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.payloads.ResizePayload;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class EventsTest {

    @Test
    public final void testDispatch() {
        Events.getInstance().addListener(event -> {
            assertEquals(event.message, 1);
            return false;
        }, EventType.PAUSE.getOrdinal());

        Events.dispatch(EventType.PAUSE);
        Events.update();
    }

    @Test
    public final void testDispatchWithPayload() {
        final int testWidth = 1920;
        final int testHeight = 1080;

        Events.getInstance().addListener(event -> {
            assertEquals(
                    ((ResizePayload) event.extraInfo).getWidth(),
                    testWidth
            );
            assertEquals(
                    ((ResizePayload) event.extraInfo).getHeight(),
                    testHeight
            );
            return false;
        }, EventType.RESIZE.getOrdinal());

        Events.dispatch(
                EventType.RESIZE,
                new ResizePayload(testWidth, testHeight)
        );
        Events.update();
    }
}
