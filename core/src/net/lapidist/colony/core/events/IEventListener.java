package net.lapidist.colony.core.events;

public interface IEventListener {
    boolean handleEvent(final Event event);
}
