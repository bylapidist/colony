package net.lapidist.colony.tests.events;

import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(GdxTestRunner.class)
public class EventsTest {

    @Test
    public void testItFires() {
        Events.fire(new TestEvent());
    }

    @Test
    public void testItListens() {
        Events.on(TestEvent.class, event -> assertThat(event.fired, is(equalTo(true))));
    }

    @Test
    public void testItFiresWithPayload() {
        Events.fire(new TestEventWithPayload("Hello World"));
    }

    @Test
    public void testItReceivesPayload() {
        Events.on(TestEventWithPayload.class, event -> assertThat(event.payload, is(equalTo("Hello World"))));
    }
}
