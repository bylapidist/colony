package net.lapidist.colony.tests.events;

import net.lapidist.colony.core.events.AbstractEvent;

class TestEventWithPayload extends AbstractEvent {
    String payload;

    TestEventWithPayload(String payload) {
        super(payload);
        this.payload = payload;
    }
}
