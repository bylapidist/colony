package net.lapidist.colony.server.events;

import net.mostlyoriginal.api.event.common.Event;

import java.nio.file.Path;

/**
 * Event fired when the server writes a game state autosave file.
 */
public record AutosaveEvent(Path location, long size) implements Event {

    public AutosaveEvent {
        // explicit constructor for future validation
    }

    @Override
    public String toString() {
        return String.format(
                "%s(location=%s, size=%d)",
                getClass().getSimpleName(),
                location,
                size
        );
    }
}
