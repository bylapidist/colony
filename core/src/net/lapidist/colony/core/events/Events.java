package net.lapidist.colony.core.events;

import net.lapidist.colony.core.events.payloads.AbstractEventPayload;

public final class Events {

    private static final EventDispatcher INSTANCE = new EventDispatcher();

    private Events() {
    }

    public static EventDispatcher getInstance() {
        return INSTANCE;
    }

    public static void dispose() {
        INSTANCE.clear();
        INSTANCE.clearListeners();
        INSTANCE.clearProviders();
        INSTANCE.clearQueue();
    }

    public static void update() {
        INSTANCE.update();
    }

    public static void setDebugEnabled(final boolean enabled) {
        INSTANCE.setDebugEnabled(enabled);
    }

    public static void dispatch(final EventType eventType) {
        dispatch(eventType, null, 0.0f);
    }

    public static void dispatch(final EventType eventType, final float delay) {
        dispatch(eventType, null, delay);
    }

    public static void dispatch(final EventType eventType, final AbstractEventPayload payload) {
        dispatch(eventType, payload, 0.0f);
    }

    public static void dispatch(
            final EventType eventType,
            final AbstractEventPayload payload,
            final float delay
    ) {
        INSTANCE.dispatchMessage(
                eventType,
                payload,
                delay
        );
    }
}
