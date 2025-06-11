package net.lapidist.colony.network;

/**
 * Convenience base class for {@link MessageHandler} implementations that
 * handle a fixed message type.
 *
 * @param <T> the message type this handler processes
 */
public abstract class AbstractMessageHandler<T> implements MessageHandler<T> {

    private final Class<T> type;

    protected AbstractMessageHandler(final Class<T> typeToHandle) {
        this.type = typeToHandle;
    }

    @Override
    public final Class<T> type() {
        return type;
    }
}
