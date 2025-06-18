package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.resources.ResourceData;
import net.lapidist.colony.components.state.resources.ResourceUpdateData;
import net.lapidist.colony.components.state.map.TileData;
import net.lapidist.colony.components.state.map.TilePos;
import net.lapidist.colony.map.MapCoordinateUtils;
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
                    .playerResources(new ResourceData(new java.util.HashMap<>(message.amounts())))
                    .build();
            stateConsumer.accept(updated);
        } else {
            TilePos pos = new TilePos(message.x(), message.y());
            TileData tile = state.getTile(pos.x(), pos.y());
            if (tile != null) {
                TileData newTile = tile.toBuilder()
                        .resources(new ResourceData(new java.util.HashMap<>(message.amounts())))
                        .build();
                int chunkX = MapCoordinateUtils.toChunkCoord(pos.x());
                int chunkY = MapCoordinateUtils.toChunkCoord(pos.y());
                int localX = MapCoordinateUtils.toLocalCoord(pos.x());
                int localY = MapCoordinateUtils.toLocalCoord(pos.y());
                state.getOrCreateChunk(chunkX, chunkY).getTiles()
                        .put(new TilePos(localX, localY), newTile);
            }
        }
    }
}
