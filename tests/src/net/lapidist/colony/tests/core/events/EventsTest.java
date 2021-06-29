package net.lapidist.colony.tests.core.events;

import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class EventsTest {

    @Test
    public final void testDispatch() {
        Events.getInstance().addListener(msg -> {
            assertEquals(msg.message, 1);
            assertEquals(msg.extraInfo, "Pause");
            return false;
        }, Events.EventType.PAUSE.getMessage());

        Events.dispatch(0,  Events.EventType.PAUSE);
        Events.update();
    }
}
