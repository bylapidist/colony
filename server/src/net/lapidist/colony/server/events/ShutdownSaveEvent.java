package net.lapidist.colony.server.events;

import net.mostlyoriginal.api.event.common.Event;

import java.nio.file.Path;

/**
 * Event fired when the server saves the game on shutdown.
 *
 * @param location path to the save file
 * @param size     file size in bytes
 */
public record ShutdownSaveEvent(Path location, long size) implements Event {

    public ShutdownSaveEvent {
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
