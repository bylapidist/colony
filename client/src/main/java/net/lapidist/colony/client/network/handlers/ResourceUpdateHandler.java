package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** Queues resource updates and applies them to the local map state. */
public final class ResourceUpdateHandler extends AbstractMessageHandler<ResourceUpdateData> {
    private final Map<Class<?>, Queue<?>> queues;
    private final Supplier<MapState> stateSupplier;
    private final Consumer<MapState> stateConsumer;

    public ResourceUpdateHandler(final Map<Class<?>, Queue<?>> queuesToUse,
                                 final Supplier<MapState> stateSupplierToUse,
                                 final Consumer<MapState> stateConsumerToUse) {
        super(ResourceUpdateData.class);
        this.queues = queuesToUse;
        this.stateSupplier = stateSupplierToUse;
        this.stateConsumer = stateConsumerToUse;
    }

    @Override
    public void handle(final ResourceUpdateData message) {
        @SuppressWarnings("unchecked")
        Queue<ResourceUpdateData> queue = (Queue<ResourceUpdateData>) queues.get(ResourceUpdateData.class);
        if (queue != null) {
            queue.add(message);
        }
        MapState state = stateSupplier.get();
        if (state == null) {
            return;
        }
        if (message.x() == -1 && message.y() == -1) {
            MapState updated = state.toBuilder()
                    .playerResources(new ResourceData(message.wood(), message.stone(), message.food()))
                    .build();
            stateConsumer.accept(updated);
        } else {
            TilePos pos = new TilePos(message.x(), message.y());
            TileData tile = state.tiles().get(pos);
            if (tile != null) {
                TileData newTile = tile.toBuilder()
                        .resources(new ResourceData(message.wood(), message.stone(), message.food()))
                        .build();
                state.tiles().put(pos, newTile);
            }
        }
    }
}
