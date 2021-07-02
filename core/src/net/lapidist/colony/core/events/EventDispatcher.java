package net.lapidist.colony.core.events;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import net.lapidist.colony.core.events.payloads.AbstractEventPayload;

public class EventDispatcher extends MessageDispatcher {


    private boolean debugEnabled = false;

    public EventDispatcher() {
    }

    public void dispatchMessage(
            final EventType eventType,
            final AbstractEventPayload payload,
            final float delay
    ) {
        dispatchMessage(delay, eventType.getOrdinal(), payload);

        if (isDebugEnabled()) {
            System.out.printf(
                    "[%s] (%s:%s) dispatched with %ss delay.%s",
                    EventDispatcher.class.getSimpleName(),
                    eventType.getOrdinal(),
                    eventType.getName(),
                    delay,
                    payload == null ? "\n" : String.format("\n\t%s\n", payload)
            );
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return this.debugEnabled;
    }

    @Override
    public void setDebugEnabled(boolean debugEnabledToSet) {
        this.debugEnabled = debugEnabledToSet;
    }
}
