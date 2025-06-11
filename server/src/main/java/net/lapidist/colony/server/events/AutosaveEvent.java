package net.lapidist.colony.server.events;

import net.mostlyoriginal.api.event.common.Event;

import java.nio.file.Path;

/**
 * Event fired when the server writes a game state autosave file.
 *
 * @param location path to the autosave file
 * @param size     file size in bytes
 */
public record AutosaveEvent(Path location, long size) implements Event {

    /**
     * Creates a new {@code AutosaveEvent}.
     */
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
