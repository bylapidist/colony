package net.lapidist.colony.server.events;

import java.nio.file.Path;

/**
 * Event fired when the server saves the game on shutdown.
 */
public final class ShutdownSaveEvent extends SaveEvent {

    public ShutdownSaveEvent(final Path locationToSet, final long sizeToSet) {
        super(locationToSet, sizeToSet);
    }
}
