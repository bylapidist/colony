package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.resources.ResourceData;
import net.lapidist.colony.components.state.resources.ResourceUpdateData;
import net.lapidist.colony.components.state.map.TileData;
import net.lapidist.colony.components.state.map.TilePos;
import net.lapidist.colony.map.MapCoordinateUtils;
import net.lapidist.colony.server.services.NetworkService;
import net.lapidist.colony.server.services.InventoryService;
import net.lapidist.colony.registry.Registries;
import java.util.Locale;

import java.util.function.Supplier;
import java.util.concurrent.locks.ReentrantLock;

/** Applies a {@link GatherCommand} to the game state and broadcasts the change. */
public final class GatherCommandHandler extends LockedCommandHandler<GatherCommand> {
    private final NetworkService networkService;
    private final InventoryService inventoryService;

    public GatherCommandHandler(final Supplier<MapState> stateSupplierToUse,
                               final java.util.function.Consumer<MapState> stateConsumerToUse,
                               final NetworkService networkServiceToUse,
                               final InventoryService inventoryServiceToUse,
                               final ReentrantLock lockToUse) {
        super(stateSupplierToUse, stateConsumerToUse, lockToUse);
        this.networkService = networkServiceToUse;
        this.inventoryService = inventoryServiceToUse;
    }

    @Override
    public Class<GatherCommand> type() {
        return GatherCommand.class;
    }

    @Override
    protected MapState modify(final GatherCommand command, final MapState state) {
        if (Registries.resources().get(command.resourceId()) == null) {
            return null;
        }
        TileData tile = state.getTile(command.x(), command.y());
        TilePos pos = new TilePos(command.x(), command.y());
        if (tile == null) {
            return null;
        }
        ResourceData res = tile.resources();
        java.util.Map<String, Integer> amounts = new java.util.HashMap<>(res.amounts());
        int current = amounts.getOrDefault(command.resourceId(), 0);
        int updatedValue = Math.max(current - 1, 0);
        amounts.put(command.resourceId(), updatedValue);
        ResourceData updated = new ResourceData(new java.util.HashMap<>(amounts));
        TileData newTile = tile.toBuilder().resources(updated).build();
        int chunkX = MapCoordinateUtils.toChunkCoord(command.x());
        int chunkY = MapCoordinateUtils.toChunkCoord(command.y());
        int localX = MapCoordinateUtils.toLocalCoord(command.x());
        int localY = MapCoordinateUtils.toLocalCoord(command.y());
        state.getOrCreateChunk(chunkX, chunkY).getTiles()
                .put(new TilePos(localX, localY), newTile);
        ResourceData player = state.playerResources();
        java.util.Map<String, Integer> playerAmounts = new java.util.HashMap<>(player.amounts());
        playerAmounts.merge(command.resourceId(), current - updatedValue, Integer::sum);
        inventoryService.addItem(command.resourceId().toLowerCase(Locale.ROOT), current - updatedValue);
        ResourceData newPlayer = new ResourceData(new java.util.HashMap<>(playerAmounts));
        MapState updatedState = state.toBuilder()
                .playerResources(newPlayer)
                .build();
        networkService.broadcast(new ResourceUpdateData(
                pos.x(),
                pos.y(),
                new java.util.HashMap<>(updated.amounts())
        ));
        return updatedState;
    }
}
