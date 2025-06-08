package net.lapidist.colony.server.handlers;

import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.network.MessageHandler;
import net.lapidist.colony.server.events.TileSelectionEvent;

import java.util.function.Supplier;

/**
 * Updates server state when a client selects a tile and broadcasts the result.
 */
public final class TileSelectionMessageHandler implements MessageHandler<TileSelectionData> {
    private final Supplier<MapState> stateSupplier;
    private final Server server;

    public TileSelectionMessageHandler(
            final Supplier<MapState> stateSupplierToUse,
            final Server serverToUse
    ) {
        this.stateSupplier = stateSupplierToUse;
        this.server = serverToUse;
    }

    @Override
    public Class<TileSelectionData> type() {
        return TileSelectionData.class;
    }

    @Override
    public void handle(final TileSelectionData data) {
        MapState state = stateSupplier.get();
        TileData tile = state.tiles().get(new TilePos(data.x(), data.y()));
        if (tile != null) {
            tile.setSelected(data.selected());
        }
        Events.dispatch(new TileSelectionEvent(data.x(), data.y(), data.selected()));
        server.sendToAllTCP(data);
    }
}
