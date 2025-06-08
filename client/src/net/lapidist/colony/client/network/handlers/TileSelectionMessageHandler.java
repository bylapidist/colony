package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.network.MessageHandler;

import java.util.Queue;

/**
 * Queues tile selection updates received from the server.
 */
public final class TileSelectionMessageHandler implements MessageHandler<TileSelectionData> {
    private final Queue<TileSelectionData> queue;

    public TileSelectionMessageHandler(final Queue<TileSelectionData> queueToUse) {
        this.queue = queueToUse;
    }

    @Override
    public Class<TileSelectionData> type() {
        return TileSelectionData.class;
    }

    @Override
    public void handle(final TileSelectionData message) {
        queue.add(message);
    }
}
