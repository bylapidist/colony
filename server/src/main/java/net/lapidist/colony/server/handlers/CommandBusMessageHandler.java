package net.lapidist.colony.server.handlers;

import net.lapidist.colony.network.AbstractMessageHandler;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.ServerCommand;

/**
 * Base class for handlers that translate network messages into
 * {@link ServerCommand} instances dispatched on the {@link CommandBus}.
 *
 * @param <T> the message type handled
 */
public abstract class CommandBusMessageHandler<T> extends AbstractMessageHandler<T> {

    private final CommandBus commandBus;

    protected CommandBusMessageHandler(final Class<T> type, final CommandBus bus) {
        super(type);
        this.commandBus = bus;
    }

    /** Convert the incoming message into a command for dispatch. */
    protected abstract ServerCommand convert(T data);

    @Override
    public final void handle(final T data) {
        commandBus.dispatch(convert(data));
    }
}
