package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.server.services.NetworkService;
import net.lapidist.colony.server.services.InventoryService;
import net.lapidist.colony.registry.Registries;
import java.util.Locale;

import java.util.function.Supplier;
import java.util.concurrent.locks.ReentrantLock;

/** Applies a {@link GatherCommand} to the game state and broadcasts the change. */
public final class GatherCommandHandler implements CommandHandler<GatherCommand> {
    private final Supplier<MapState> stateSupplier;
    private final java.util.function.Consumer<MapState> stateConsumer;
    private final NetworkService networkService;
    private final InventoryService inventoryService;
    private final ReentrantLock lock;

    public GatherCommandHandler(final Supplier<MapState> stateSupplierToUse,
                               final java.util.function.Consumer<MapState> stateConsumerToUse,
                               final NetworkService networkServiceToUse,
                               final InventoryService inventoryServiceToUse,
                               final ReentrantLock lockToUse) {
        this.stateSupplier = stateSupplierToUse;
        this.stateConsumer = stateConsumerToUse;
        this.networkService = networkServiceToUse;
        this.inventoryService = inventoryServiceToUse;
        this.lock = lockToUse;
    }

    @Override
    public Class<GatherCommand> type() {
        return GatherCommand.class;
    }

    @Override
    public void handle(final GatherCommand command) {
        lock.lock();
        try {
            MapState state = stateSupplier.get();
            if (Registries.resources().get(command.resourceId()) == null) {
                return;
            }
            TileData tile = state.getTile(command.x(), command.y());
            TilePos pos = new TilePos(command.x(), command.y());
            if (tile == null) {
                return;
            }
            ResourceData res = tile.resources();
            java.util.Map<String, Integer> amounts = new java.util.HashMap<>(res.amounts());
            int current = amounts.getOrDefault(command.resourceId(), 0);
            int updatedValue = Math.max(current - 1, 0);
            amounts.put(command.resourceId(), updatedValue);
            ResourceData updated = new ResourceData(new java.util.HashMap<>(amounts));
            TileData newTile = tile.toBuilder().resources(updated).build();
            int chunkX = Math.floorDiv(command.x(), MapChunkData.CHUNK_SIZE);
            int chunkY = Math.floorDiv(command.y(), MapChunkData.CHUNK_SIZE);
            int localX = Math.floorMod(command.x(), MapChunkData.CHUNK_SIZE);
            int localY = Math.floorMod(command.y(), MapChunkData.CHUNK_SIZE);
            state.getOrCreateChunk(chunkX, chunkY).getTiles().put(new TilePos(localX, localY), newTile);
            ResourceData player = state.playerResources();
            java.util.Map<String, Integer> playerAmounts = new java.util.HashMap<>(player.amounts());
            playerAmounts.merge(command.resourceId(), current - updatedValue, Integer::sum);
            inventoryService.addItem(command.resourceId().toLowerCase(Locale.ROOT), current - updatedValue);
            state = stateSupplier.get();
            ResourceData newPlayer = new ResourceData(new java.util.HashMap<>(playerAmounts));
            MapState updatedState = state.toBuilder()
                    .playerResources(newPlayer)
                    .build();
            stateConsumer.accept(updatedState);
            networkService.broadcast(new ResourceUpdateData(
                    pos.x(),
                    pos.y(),
                    new java.util.HashMap<>(updated.amounts())
            ));
        } finally {
            lock.unlock();
        }
    }
}
