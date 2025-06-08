package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.Queue;

/**
 * Queues tile selection updates received from the server.
 */
public final class TileSelectionUpdateHandler extends AbstractMessageHandler<TileSelectionData> {
    private final Queue<TileSelectionData> queue;

    public TileSelectionUpdateHandler(final Queue<TileSelectionData> queueToUse) {
        super(TileSelectionData.class);
        this.queue = queueToUse;
    }

    @Override
    public void handle(final TileSelectionData message) {
        queue.add(message);
    }
}
