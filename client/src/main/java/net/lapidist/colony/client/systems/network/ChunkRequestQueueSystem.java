package net.lapidist.colony.client.systems.network;

import com.artemis.BaseSystem;
import net.lapidist.colony.client.network.GameClient;

/**
 * Sends queued chunk requests in batches each frame.
 *
 * @see net.lapidist.colony.server.handlers.MapChunkRequestHandler
 */
public final class ChunkRequestQueueSystem extends BaseSystem {
    private final GameClient client;

    public ChunkRequestQueueSystem(final GameClient clientToUse) {
        this.client = clientToUse;
    }

    @Override
    protected void processSystem() {
        client.processChunkRequestQueue();
    }
}
