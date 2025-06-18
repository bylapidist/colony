package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.map.MapCoordinateUtils;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.server.events.TileSelectionEvent;
import net.lapidist.colony.server.services.NetworkService;

import java.util.function.Supplier;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Applies a {@link TileSelectionCommand} to the game state and broadcasts the change.
 */
public final class TileSelectionCommandHandler extends LockedCommandHandler<TileSelectionCommand> {
    private final NetworkService networkService;

    public TileSelectionCommandHandler(final Supplier<MapState> stateSupplierToUse,
                                       final NetworkService networkServiceToUse,
                                       final ReentrantLock lockToUse) {
        super(stateSupplierToUse, s -> { }, lockToUse);
        this.networkService = networkServiceToUse;
    }

    @Override
    public Class<TileSelectionCommand> type() {
        return TileSelectionCommand.class;
    }

    @Override
    protected MapState modify(final TileSelectionCommand command, final MapState state) {
        TileData tile = state.getTile(command.x(), command.y());
        if (tile != null) {
            TileData updated = tile.toBuilder().selected(command.selected()).build();
            int chunkX = MapCoordinateUtils.toChunkCoord(command.x());
            int chunkY = MapCoordinateUtils.toChunkCoord(command.y());
            int localX = MapCoordinateUtils.toLocalCoord(command.x());
            int localY = MapCoordinateUtils.toLocalCoord(command.y());
            state.getOrCreateChunk(chunkX, chunkY).getTiles()
                    .put(new TilePos(localX, localY), updated);
        }
        Events.dispatch(new TileSelectionEvent(command.x(), command.y(), command.selected()));
        networkService.broadcast(new TileSelectionData(command.x(), command.y(), command.selected()));
        return state;
    }
}
