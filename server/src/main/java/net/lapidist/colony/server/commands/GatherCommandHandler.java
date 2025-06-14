package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.server.services.NetworkService;

import java.util.function.Supplier;
import java.util.concurrent.locks.ReentrantLock;

/** Applies a {@link GatherCommand} to the game state and broadcasts the change. */
public final class GatherCommandHandler implements CommandHandler<GatherCommand> {
    private final Supplier<MapState> stateSupplier;
    private final java.util.function.Consumer<MapState> stateConsumer;
    private final NetworkService networkService;
    private final ReentrantLock lock;

    public GatherCommandHandler(final Supplier<MapState> stateSupplierToUse,
                               final java.util.function.Consumer<MapState> stateConsumerToUse,
                               final NetworkService networkServiceToUse,
                               final ReentrantLock lockToUse) {
        this.stateSupplier = stateSupplierToUse;
        this.stateConsumer = stateConsumerToUse;
        this.networkService = networkServiceToUse;
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
            TileData tile = state.getTile(command.x(), command.y());
            TilePos pos = new TilePos(command.x(), command.y());
            if (tile == null) {
                return;
            }
            ResourceData res = tile.resources();
            ResourceData updated;
            switch (command.resourceType()) {
                case "WOOD" -> updated = new ResourceData(Math.max(res.wood() - 1, 0), res.stone(), res.food());
                case "STONE" -> updated = new ResourceData(res.wood(), Math.max(res.stone() - 1, 0), res.food());
                case "FOOD" -> updated = new ResourceData(res.wood(), res.stone(), Math.max(res.food() - 1, 0));
                default -> updated = res;
            }
            TileData newTile = tile.toBuilder().resources(updated).build();
            int chunkX = Math.floorDiv(command.x(), MapChunkData.CHUNK_SIZE);
            int chunkY = Math.floorDiv(command.y(), MapChunkData.CHUNK_SIZE);
            int localX = Math.floorMod(command.x(), MapChunkData.CHUNK_SIZE);
            int localY = Math.floorMod(command.y(), MapChunkData.CHUNK_SIZE);
            state.getOrCreateChunk(chunkX, chunkY).getTiles().put(new TilePos(localX, localY), newTile);
            ResourceData player = state.playerResources();
            ResourceData newPlayer = new ResourceData(
                    player.wood() + (res.wood() - updated.wood()),
                    player.stone() + (res.stone() - updated.stone()),
                    player.food() + (res.food() - updated.food())
            );
            MapState updatedState = state.toBuilder()
                    .playerResources(newPlayer)
                    .build();
            stateConsumer.accept(updatedState);
            networkService.broadcast(new ResourceUpdateData(
                    pos.x(),
                    pos.y(),
                    updated.wood(),
                    updated.stone(),
                    updated.food()
            ));
        } finally {
            lock.unlock();
        }
    }
}
