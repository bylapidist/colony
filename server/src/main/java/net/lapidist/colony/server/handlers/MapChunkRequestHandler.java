package net.lapidist.colony.server.handlers;

import net.lapidist.colony.components.state.MapChunkRequest;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.network.AbstractMessageHandler;
import net.lapidist.colony.server.services.NetworkService;

import java.util.function.Supplier;

/** Handles incoming {@link MapChunkRequest} messages by sending the requested chunk. */
public final class MapChunkRequestHandler extends AbstractMessageHandler<MapChunkRequest> {
    private final Supplier<MapState> stateSupplier;
    private final NetworkService networkService;

    public MapChunkRequestHandler(final Supplier<MapState> stateSupplierToUse,
                                  final NetworkService networkServiceToUse) {
        super(MapChunkRequest.class);
        this.stateSupplier = stateSupplierToUse;
        this.networkService = networkServiceToUse;
    }

    @Override
    public void handle(final MapChunkRequest message) {
        MapState state = stateSupplier.get();
        if (state != null) {
            networkService.broadcastChunk(state, message.chunkX(), message.chunkY());
        }
    }
}
