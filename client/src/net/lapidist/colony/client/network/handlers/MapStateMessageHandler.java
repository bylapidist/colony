package net.lapidist.colony.client.network.handlers;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.network.MessageHandler;

import java.util.function.Consumer;

/**
 * Applies the received {@link MapState} to the client.
 */
public final class MapStateMessageHandler implements MessageHandler<MapState> {
    private final Consumer<MapState> consumer;

    public MapStateMessageHandler(final Consumer<MapState> consumerToUse) {
        this.consumer = consumerToUse;
    }

    @Override
    public Class<MapState> type() {
        return MapState.class;
    }

    @Override
    public void handle(final MapState message) {
        consumer.accept(message);
    }
}
