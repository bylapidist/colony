package net.lapidist.colony.events;

import net.mostlyoriginal.api.event.common.Event;
import net.mostlyoriginal.api.event.common.EventSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

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
    private static final Map<Class<? extends Event>, List<Consumer<? extends Event>>>
            LISTENERS = new ConcurrentHashMap<>();

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

    public static <T extends Event> void listen(final Class<T> type, final Consumer<T> handler) {
        LISTENERS.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>()).add(handler);
    }

    public static void dispatch(final Event event) {
        if (instance != null) {
            LOGGER.debug("Dispatched event: {}", event);
            instance.dispatch(event);
        }
        for (Map.Entry<Class<? extends Event>, List<Consumer<? extends Event>>> e : LISTENERS.entrySet()) {
            if (e.getKey().isInstance(event)) {
                for (Consumer handler : e.getValue()) {
                    handler.accept(event);
                }
            }
        }
    }

    public static void update() {
        if (instance != null) {
            instance.process();
        }
    }

    public static void dispose() {
        instance = null;
        LISTENERS.clear();
    }
}
