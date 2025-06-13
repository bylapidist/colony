package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.map.MapChunkData;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.server.events.TileSelectionEvent;
import net.lapidist.colony.server.services.NetworkService;

import java.util.function.Supplier;

/**
 * Applies a {@link TileSelectionCommand} to the game state and broadcasts the change.
 */
public final class TileSelectionCommandHandler implements CommandHandler<TileSelectionCommand> {
    private final Supplier<MapState> stateSupplier;
    private final NetworkService networkService;

    public TileSelectionCommandHandler(final Supplier<MapState> stateSupplierToUse,
                                       final NetworkService networkServiceToUse) {
        this.stateSupplier = stateSupplierToUse;
        this.networkService = networkServiceToUse;
    }

    @Override
    public Class<TileSelectionCommand> type() {
        return TileSelectionCommand.class;
    }

    @Override
    public void handle(final TileSelectionCommand command) {
        MapState state = stateSupplier.get();
        TileData tile = state.getTile(command.x(), command.y());
        if (tile != null) {
            TileData updated = tile.toBuilder().selected(command.selected()).build();
            int chunkX = Math.floorDiv(command.x(), MapChunkData.CHUNK_SIZE);
            int chunkY = Math.floorDiv(command.y(), MapChunkData.CHUNK_SIZE);
            int localX = Math.floorMod(command.x(), MapChunkData.CHUNK_SIZE);
            int localY = Math.floorMod(command.y(), MapChunkData.CHUNK_SIZE);
            state.getOrCreateChunk(chunkX, chunkY).getTiles().put(new TilePos(localX, localY), updated);
        }
        Events.dispatch(new TileSelectionEvent(command.x(), command.y(), command.selected()));
        networkService.broadcast(new TileSelectionData(command.x(), command.y(), command.selected()));
    }
}
