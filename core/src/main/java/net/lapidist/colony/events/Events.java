package net.lapidist.colony.events;

import net.mostlyoriginal.api.event.common.Event;
import net.mostlyoriginal.api.event.common.EventSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Events {

    /**
     * Shared event system instance.
     *
     * <p>The instance is accessed from the network and autosave threads on the
     * server, so visibility between threads is required. The game server and
     * client call {@link #init(EventSystem)} during start up before any worker
     * threads are spawned.</p>
     */
    private static volatile EventSystem instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(Events.class);

    private Events() { }

    /**
     * Set the global event system instance.
     *
     * <p>Call this before starting any threads that dispatch or process events
     * (for example the server network thread or autosave scheduler). The
     * instance is stored in a {@code volatile} field to ensure visibility between
     * threads.</p>
     */
    public static void init(final EventSystem system) {
        instance = system;
    }

    public static EventSystem getInstance() {
        return instance;
    }

    public static void dispatch(final Event event) {
        if (instance != null) {
            LOGGER.debug("Dispatched event: {}", event);
            instance.dispatch(event);
        }
    }

    public static void update() {
        if (instance != null) {
            instance.process();
        }
    }

    public static void dispose() {
        instance = null;
    }
}
