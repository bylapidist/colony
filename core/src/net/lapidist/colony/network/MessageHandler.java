package net.lapidist.colony.network;

/**
 * Handles a specific type of network message.
 *
 * @param <T> the message type
 */
public interface MessageHandler<T> {
    /**
     * @return the class of message this handler processes
     */
    Class<T> type();

    /**
     * Process the given message instance.
     *
     * @param message the message to handle
     */
    void handle(T message);

    /**
     * Register this handler with the supplied endpoint.
     *
     * @param endpoint the endpoint to register with
     */
    default void register(final MessageEndpoint endpoint) {
        endpoint.onMessage(type(), this::handle);
    }
}
