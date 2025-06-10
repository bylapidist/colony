package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.Map;
import java.util.Queue;

/**
 * Queues building updates received from the server.
 */
public final class BuildingUpdateHandler extends AbstractMessageHandler<BuildingData> {
    private final Map<Class<?>, Queue<?>> queues;

    public BuildingUpdateHandler(final Map<Class<?>, Queue<?>> queuesToUse) {
        super(BuildingData.class);
        this.queues = queuesToUse;
    }

    @Override
    public void handle(final BuildingData message) {
        @SuppressWarnings("unchecked")
        Queue<BuildingData> queue = (Queue<BuildingData>) queues.get(BuildingData.class);
        if (queue != null) {
            queue.add(message);
        }
    }
}
