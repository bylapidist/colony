package net.lapidist.colony.server.events;

import net.mostlyoriginal.api.event.common.Event;

import java.nio.file.Path;

/** Event fired when the server starts writing a game state autosave file. */
public record AutosaveStartEvent(Path location) implements Event {

    /**
     * Creates a new {@code AutosaveStartEvent}.
     */
    public AutosaveStartEvent {
        // explicit constructor for future validation
    }

    @Override
    public String toString() {
        return String.format(
                "%s(location=%s)",
                getClass().getSimpleName(),
                location
        );
    }
}
