package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.components.state.map.MapMetadata;
import net.lapidist.colony.network.AbstractMessageHandler;

import java.util.function.Consumer;

/**
 * Receives map metadata and triggers a callback.
 */
public final class MapMetadataHandler extends AbstractMessageHandler<MapMetadata> {
    private final Consumer<MapMetadata> consumer;

    public MapMetadataHandler(final Consumer<MapMetadata> consumerToUse) {
        super(MapMetadata.class);
        this.consumer = consumerToUse;
    }

    @Override
    public void handle(final MapMetadata message) {
        consumer.accept(message);
    }
}
