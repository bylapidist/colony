package net.lapidist.colony.server.events;

import net.mostlyoriginal.api.event.common.Event;

import java.nio.file.Path;
// Path is used instead of FileHandle so this event class can remain independent
// from the LibGDX runtime which is not loaded on the server.

/**
 * Event fired when the server writes a game state autosave file.
 */
public final class AutosaveEvent implements Event {
    private final Path location;
    private final long size;

    public AutosaveEvent(final Path locationToSet, final long sizeToSet) {
        this.location = locationToSet;
        this.size = sizeToSet;
    }

    public Path getLocation() {
        return location;
    }

    public long getSize() {
        return size;
    }
}
