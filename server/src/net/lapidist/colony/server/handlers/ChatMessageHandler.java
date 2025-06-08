package net.lapidist.colony.server.handlers;

import net.lapidist.colony.chat.ChatMessage;
import net.lapidist.colony.network.AbstractMessageHandler;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.TileSelectionCommand;
import net.lapidist.colony.server.services.NetworkService;

/**
 * Handles incoming chat messages and broadcasts them to all clients.
 * Messages starting with a slash are treated as commands.
 */
public final class ChatMessageHandler extends AbstractMessageHandler<ChatMessage> {
    private static final int SELECT_PARTS = 4;
    private static final int ARG_X = 1;
    private static final int ARG_Y = 2;
    private static final int ARG_SELECTED = 3;
    private final NetworkService networkService;
    private final CommandBus commandBus;

    public ChatMessageHandler(final NetworkService service, final CommandBus bus) {
        super(ChatMessage.class);
        this.networkService = service;
        this.commandBus = bus;
    }

    @Override
    public void handle(final ChatMessage message) {
        String text = message.text();
        if (text.startsWith("/")) {
            processCommand(text.substring(1));
        } else {
            networkService.broadcast(message);
        }
    }

    private void processCommand(final String commandLine) {
        String[] parts = commandLine.split("\\s+");
        if (parts.length == SELECT_PARTS && "select".equals(parts[0])) {
            try {
                int x = Integer.parseInt(parts[ARG_X]);
                int y = Integer.parseInt(parts[ARG_Y]);
                boolean selected = Boolean.parseBoolean(parts[ARG_SELECTED]);
                commandBus.dispatch(new TileSelectionCommand(x, y, selected));
            } catch (NumberFormatException ignore) {
                // silently ignore malformed commands
            }
        }
    }
}
