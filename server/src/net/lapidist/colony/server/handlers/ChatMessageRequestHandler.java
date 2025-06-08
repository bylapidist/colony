package net.lapidist.colony.server.handlers;

import net.lapidist.colony.components.state.ChatMessageData;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.function.Consumer;

/**
 * Broadcasts chat messages received from clients.
 */
public final class ChatMessageRequestHandler extends AbstractMessageHandler<ChatMessageData> {
    private final Consumer<ChatMessageData> broadcaster;

    public ChatMessageRequestHandler(final Consumer<ChatMessageData> broadcasterToUse) {
        super(ChatMessageData.class);
        this.broadcaster = broadcasterToUse;
    }

    @Override
    public void handle(final ChatMessageData data) {
        broadcaster.accept(data);
    }
}
