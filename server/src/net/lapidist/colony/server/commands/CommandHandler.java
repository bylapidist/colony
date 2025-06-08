package net.lapidist.colony.server.commands;

/**
 * Handles a specific type of {@link ServerCommand}.
 *
 * @param <C> the command type
 */
public interface CommandHandler<C extends ServerCommand> {
    /**
     * @return the class of command this handler processes
     */
    Class<C> type();

    /**
     * Process the given command instance.
     *
     * @param command the command to handle
     */
    void handle(C command);

    /**
     * Register this handler with the supplied bus.
     *
     * @param bus the bus to register with
     */
    default void register(final CommandBus bus) {
        bus.register(type(), this);
    }
}
