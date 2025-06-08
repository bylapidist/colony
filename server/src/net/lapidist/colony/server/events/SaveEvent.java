package net.lapidist.colony.server.events;

import net.lapidist.colony.core.events.AbstractDataEvent;

import java.nio.file.Path;

/**
 * Base class for events triggered when the game state is saved.
 */
public abstract class SaveEvent extends AbstractDataEvent {
    private final Path location;
    private final long size;

    protected SaveEvent(final Path locationToSet, final long sizeToSet) {
        this.location = locationToSet;
        this.size = sizeToSet;
    }

    public final Path getLocation() {
        return location;
    }

    public final long getSize() {
        return size;
    }

    @Override
    public final String toString() {
        return format("location", location, "size", size);
    }
}
