package net.lapidist.colony.server.commands;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple dispatcher mapping commands to their handlers.
 */
public final class CommandBus {

    private final Map<Class<?>, CommandHandler<?>> handlers = new ConcurrentHashMap<>();

    public <C extends ServerCommand> void register(final Class<C> type, final CommandHandler<? super C> handler) {
        handlers.put(type, handler);
    }

    public void registerHandlers(final Iterable<? extends CommandHandler<?>> handlersToRegister) {
        for (CommandHandler<?> handler : handlersToRegister) {
            handler.register(this);
        }
    }

    @SuppressWarnings("unchecked")
    public <C extends ServerCommand> void dispatch(final C command) {
        CommandHandler<C> handler = (CommandHandler<C>) handlers.get(command.getClass());
        if (handler != null) {
            handler.handle(command);
        }
    }
}
