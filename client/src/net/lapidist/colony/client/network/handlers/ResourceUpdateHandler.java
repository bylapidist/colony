package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.Queue;

/** Queues resource updates received from the server. */
public final class ResourceUpdateHandler extends AbstractMessageHandler<ResourceUpdateData> {
    private final Queue<ResourceUpdateData> queue;

    public ResourceUpdateHandler(final Queue<ResourceUpdateData> queueToUse) {
        super(ResourceUpdateData.class);
        this.queue = queueToUse;
    }

    @Override
    public void handle(final ResourceUpdateData message) {
        queue.add(message);
    }
}
