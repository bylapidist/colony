package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.chat.ChatMessage;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.Queue;

/**
 * Queues chat messages received from the server.
 */
public final class ChatMessageHandler extends AbstractMessageHandler<ChatMessage> {
    private final Queue<ChatMessage> queue;

    public ChatMessageHandler(final Queue<ChatMessage> queueToUse) {
        super(ChatMessage.class);
        this.queue = queueToUse;
    }

    @Override
    public void handle(final ChatMessage message) {
        queue.add(message);
    }
}
