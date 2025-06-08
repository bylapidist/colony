package net.lapidist.colony.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Simple message dispatcher that maps message classes to handlers.
 */
public final class MessageDispatcher {

    private final Map<Class<?>, Consumer<Object>> handlers = new ConcurrentHashMap<>();

    public <T> void register(final Class<T> type, final Consumer<T> handler) {
        handlers.put(type, (Consumer<Object>) handler);
    }

    public void dispatch(final Object message) {
        Consumer<Object> handler = handlers.get(message.getClass());
        if (handler != null) {
            handler.accept(message);
        }
    }
}
