package net.lapidist.colony.events;

import net.mostlyoriginal.api.event.common.Event;

/**
 * Base event providing a helper for consistent {@code toString()} output
 * for events carrying data fields.
 */
public abstract class AbstractDataEvent implements Event {

    /**
     * Builds a standard event string using pairs of field names and values.
     *
     * @param parts alternating field names and values
     * @return formatted event string
     */
    protected final String format(final Object... parts) {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append('(');
        for (int i = 0; i < parts.length; i += 2) {
            builder.append(parts[i]).append('=');
            builder.append(parts[i + 1]);
            if (i < parts.length - 2) {
                builder.append(", ");
            }
        }
        builder.append(')');
        return builder.toString();
    }
}
