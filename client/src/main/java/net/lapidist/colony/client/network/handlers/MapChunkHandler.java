package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.components.state.MapChunk;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.function.Consumer;

/**
 * Receives map chunk data and triggers a callback.
 */
public final class MapChunkHandler extends AbstractMessageHandler<MapChunk> {
    private final Consumer<MapChunk> consumer;

    public MapChunkHandler(final Consumer<MapChunk> consumerToUse) {
        super(MapChunk.class);
        this.consumer = consumerToUse;
    }

    @Override
    public void handle(final MapChunk message) {
        consumer.accept(message);
    }
}
