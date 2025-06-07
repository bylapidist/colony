package net.lapidist.colony.server.events;

import java.nio.file.Path;
// Path is used instead of FileHandle so this event class can remain independent
// from the LibGDX runtime which is not loaded on the server.

/**
 * Event fired when the server writes a game state autosave file.
 */
public final class AutosaveEvent extends SaveEvent {

    public AutosaveEvent(final Path locationToSet, final long sizeToSet) {
        super(locationToSet, sizeToSet);
    }
}
