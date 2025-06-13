package net.lapidist.colony.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Simple message dispatcher that maps message classes to handlers.
 */
public final class MessageDispatcher {

    private final Map<Class<?>, Consumer<?>> handlers = new ConcurrentHashMap<>();

    /**
     * Registers a handler for the given message type.
     *
     * @param type    the message class to handle
     * @param handler consumer that processes messages of {@code type}
     */
    public <T> void register(final Class<T> type, final Consumer<? super T> handler) {
        handlers.put(type, handler);
    }

    /**
     * Dispatches the supplied message to the registered handler if present. The
     * dispatcher first checks for an exact type match and then searches for any
     * handler whose registered type is assignable from the message class.
     *
     * @param message the message to dispatch
     */
    @SuppressWarnings("unchecked")
    public <T> void dispatch(final T message) {
        Class<?> messageType = message.getClass();
        Consumer<T> handler = (Consumer<T>) handlers.get(messageType);
        if (handler == null) {
            for (Map.Entry<Class<?>, Consumer<?>> entry : handlers.entrySet()) {
                if (entry.getKey().isAssignableFrom(messageType)) {
                    handler = (Consumer<T>) entry.getValue();
                    break;
                }
            }
        }
        if (handler != null) {
            handler.accept(message);
        }
    }
}
