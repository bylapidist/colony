package net.lapidist.colony.server.handlers;

import com.esotericsoftware.kryonet.Server;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.network.AbstractMessageHandler;
import net.lapidist.colony.server.events.TileSelectionEvent;

import java.util.function.Supplier;

/**
 * Updates server state when a client selects a tile and broadcasts the result.
 */
public final class TileSelectionMessageHandler extends AbstractMessageHandler<TileSelectionData> {
    private final Supplier<MapState> stateSupplier;
    private final Server server;

    public TileSelectionMessageHandler(
            final Supplier<MapState> stateSupplierToUse,
            final Server serverToUse
    ) {
        super(TileSelectionData.class);
        this.stateSupplier = stateSupplierToUse;
        this.server = serverToUse;
    }

    @Override
    public void handle(final TileSelectionData data) {
        MapState state = stateSupplier.get();
        TilePos pos = new TilePos(data.x(), data.y());
        TileData tile = state.tiles().get(pos);
        if (tile != null) {
            TileData updated = tile.toBuilder().selected(data.selected()).build();
            state.tiles().put(pos, updated);
        }
        Events.dispatch(new TileSelectionEvent(data.x(), data.y(), data.selected()));
        server.sendToAllTCP(data);
    }
}
