package net.lapidist.colony.tests.events;

import net.lapidist.colony.core.events.EventQueue;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(GdxTestRunner.class)
public class EventQueueTest {

    private EventQueue eventQueue = new EventQueue();

    @Test
    public void testItQueuesEvents() {
        eventQueue.add(new TestEvent());
        eventQueue.add(new TestEventWithPayload("Hello world"));
    }

    @Test
    public void testItListens() {
        Events.on(TestEvent.class, event -> assertThat(event.fired, is(equalTo(true))));
        Events.on(TestEventWithPayload.class, event -> assertThat(event.payload, is(equalTo("Hello World"))));
    }

    @Test
    public void testItRuns() {
        eventQueue.run();
    }
}
