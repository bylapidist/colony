package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.Map;
import java.util.Queue;

/**
 * Queues tile selection updates received from the server.
 */
public final class TileSelectionUpdateHandler extends AbstractMessageHandler<TileSelectionData> {
    private final Map<Class<?>, Queue<?>> queues;

    public TileSelectionUpdateHandler(final Map<Class<?>, Queue<?>> queuesToUse) {
        super(TileSelectionData.class);
        this.queues = queuesToUse;
    }

    @Override
    public void handle(final TileSelectionData message) {
        @SuppressWarnings("unchecked")
        Queue<TileSelectionData> queue = (Queue<TileSelectionData>) queues.get(TileSelectionData.class);
        if (queue != null) {
            queue.add(message);
        }
    }
}
