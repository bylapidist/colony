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
    private static final List<Object> PENDING = new CopyOnWriteArrayList<>();

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
        for (Object listener : PENDING) {
            instance.registerEvents(listener);
        }
        PENDING.clear();
    }

    public static EventSystem getInstance() {
        return instance;
    }

    /**
     * Register a listener object with the global event system. If the system is
     * not yet initialised the listener is queued and automatically registered
     * once {@link #init(EventSystem)} is called.
     */
    public static void registerEvents(final Object listener) {
        if (instance != null) {
            instance.registerEvents(listener);
        } else {
            PENDING.add(listener);
        }
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
        PENDING.clear();
    }
}
