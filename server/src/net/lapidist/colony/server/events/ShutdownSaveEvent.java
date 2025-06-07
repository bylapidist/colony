package net.lapidist.colony.server.events;

import net.mostlyoriginal.api.event.common.Event;

import java.nio.file.Path;

/**
 * Event fired when the server saves the game on shutdown.
 */
public final class ShutdownSaveEvent implements Event {
    private final Path location;
    private final long size;

    public ShutdownSaveEvent(final Path locationToSet, final long sizeToSet) {
        this.location = locationToSet;
        this.size = sizeToSet;
    }

    public Path getLocation() {
        return location;
    }

    public long getSize() {
        return size;
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
