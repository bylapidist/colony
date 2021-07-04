package net.lapidist.colony.core.events;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.events.payloads.AbstractEventPayload;

public final class EventDispatcher extends MessageDispatcher {

    public EventDispatcher() {
    }

    public void dispatchMessage(
            final EventType eventType,
            final AbstractEventPayload payload,
            final float delay
    ) {
        dispatchMessage(delay, eventType.getOrdinal(), payload);

        if (Constants.DEBUG) {
            System.out.printf(
                    "[%s] (%s:%s) dispatched with %ss delay.%s",
                    EventDispatcher.class.getSimpleName(),
                    eventType.getOrdinal(),
                    eventType.getName(),
                    delay,
                    payload == null ? "\n" : String.format("\n%s\n", payload)
            );
        }
    }
}
