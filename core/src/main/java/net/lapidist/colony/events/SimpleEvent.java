package net.lapidist.colony.events;

import net.mostlyoriginal.api.event.common.Event;

/**
 * Base class for events without any additional data.
 */
public abstract class SimpleEvent implements Event {

    @Override
    public final String toString() {
        return getClass().getSimpleName();
    }
}
