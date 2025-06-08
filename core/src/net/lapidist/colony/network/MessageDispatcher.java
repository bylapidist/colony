package net.lapidist.colony.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Simple message dispatcher that maps message classes to handlers.
 */
public final class MessageDispatcher {

    private final Map<Class<?>, Consumer<?>> handlers = new ConcurrentHashMap<>();

    public <T> void register(final Class<T> type, final Consumer<? super T> handler) {
        handlers.put(type, handler);
    }

    @SuppressWarnings("unchecked")
    public <T> void dispatch(final T message) {
        Consumer<T> handler = (Consumer<T>) handlers.get(message.getClass());
        if (handler != null) {
            handler.accept(message);
        }
    }
}
