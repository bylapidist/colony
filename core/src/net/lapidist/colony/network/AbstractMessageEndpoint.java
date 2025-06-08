package net.lapidist.colony.network;

import java.util.function.Consumer;

/**
 * Base implementation of {@link MessageEndpoint} providing common dispatch
 * logic for client and server endpoints.
 */
public abstract class AbstractMessageEndpoint implements MessageEndpoint {

    private final MessageDispatcher dispatcher = new MessageDispatcher();

    /**
     * Dispatches a received message to registered handlers.
     *
     * @param message the message to dispatch
     */
    protected void dispatch(final Object message) {
        dispatcher.dispatch(message);
    }

    /**
     * Registers all handlers with this endpoint.
     *
     * @param handlers handlers to register
     */
    protected final void registerHandlers(final Iterable<? extends MessageHandler<?>> handlers) {
        for (MessageHandler<?> handler : handlers) {
            handler.register(this);
        }
    }

    @Override
    public final <T> void onMessage(final Class<T> type, final Consumer<T> handler) {
        dispatcher.register(type, handler);
    }
}
