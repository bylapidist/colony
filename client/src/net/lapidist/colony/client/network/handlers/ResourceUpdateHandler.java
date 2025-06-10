package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.Map;
import java.util.Queue;

/** Queues resource updates received from the server. */
public final class ResourceUpdateHandler extends AbstractMessageHandler<ResourceUpdateData> {
    private final Map<Class<?>, Queue<?>> queues;

    public ResourceUpdateHandler(final Map<Class<?>, Queue<?>> queuesToUse) {
        super(ResourceUpdateData.class);
        this.queues = queuesToUse;
    }

    @Override
    public void handle(final ResourceUpdateData message) {
        @SuppressWarnings("unchecked")
        Queue<ResourceUpdateData> queue = (Queue<ResourceUpdateData>) queues.get(ResourceUpdateData.class);
        if (queue != null) {
            queue.add(message);
        }
    }
}
