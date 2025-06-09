package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.Queue;

/**
 * Queues building updates received from the server.
 */
public final class BuildingUpdateHandler extends AbstractMessageHandler<BuildingData> {
    private final Queue<BuildingData> queue;

    public BuildingUpdateHandler(final Queue<BuildingData> queueToUse) {
        super(BuildingData.class);
        this.queue = queueToUse;
    }

    @Override
    public void handle(final BuildingData message) {
        queue.add(message);
    }
}
