package net.lapidist.colony.tests.events;

import net.lapidist.colony.core.events.AbstractEvent;

class TestEvent extends AbstractEvent {
    boolean fired;

    TestEvent() {
        super("Hello World");
        fired = true;
    }
}
