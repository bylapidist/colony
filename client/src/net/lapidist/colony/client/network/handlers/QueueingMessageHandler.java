package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.Map;
import java.util.Queue;

/**
 * Generic handler that queues incoming messages by type.
 *
 * @param <T> the message type to handle
 */
public final class QueueingMessageHandler<T> extends AbstractMessageHandler<T> {
    private final Map<Class<?>, Queue<?>> queues;

    public QueueingMessageHandler(final Class<T> type, final Map<Class<?>, Queue<?>> queuesToUse) {
        super(type);
        this.queues = queuesToUse;
    }

    @Override
    public void handle(final T message) {
        @SuppressWarnings("unchecked")
        Queue<T> queue = (Queue<T>) queues.get(type());
        if (queue != null) {
            queue.add(message);
        }
    }
}
