package net.lapidist.colony.core.events;

public interface IEventProvider {
    IEventPayload provideEventPayload(final int eventType, final IEventListener eventReceiver);
}
