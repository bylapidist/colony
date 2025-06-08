package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.components.state.ChatMessageData;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.Queue;

/**
 * Queues chat messages received from the server.
 */
public final class ChatMessageUpdateHandler extends AbstractMessageHandler<ChatMessageData> {
    private final Queue<ChatMessageData> queue;

    public ChatMessageUpdateHandler(final Queue<ChatMessageData> queueToUse) {
        super(ChatMessageData.class);
        this.queue = queueToUse;
    }

    @Override
    public void handle(final ChatMessageData message) {
        queue.add(message);
    }
}
