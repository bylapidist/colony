package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.chat.ChatMessage;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.Map;
import java.util.Queue;

/**
 * Queues chat messages received from the server.
 */
public final class ChatMessageHandler extends AbstractMessageHandler<ChatMessage> {
    private final Map<Class<?>, Queue<?>> queues;

    public ChatMessageHandler(final Map<Class<?>, Queue<?>> queuesToUse) {
        super(ChatMessage.class);
        this.queues = queuesToUse;
    }

    @Override
    public void handle(final ChatMessage message) {
        @SuppressWarnings("unchecked")
        Queue<ChatMessage> queue = (Queue<ChatMessage>) queues.get(ChatMessage.class);
        if (queue != null) {
            queue.add(message);
        }
    }
}
