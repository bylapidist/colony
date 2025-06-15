package net.lapidist.colony.server.handlers;

import net.lapidist.colony.network.ChatMessage;
import net.lapidist.colony.network.AbstractMessageHandler;
import java.util.HashMap;
import java.util.Map;

import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.chat.ChatCommand;
import net.lapidist.colony.server.chat.SelectChatCommand;
import net.lapidist.colony.server.services.NetworkService;

/**
 * Handles incoming chat messages and broadcasts them to all clients.
 * Messages starting with a slash are treated as commands.
 */
public final class ChatMessageHandler extends AbstractMessageHandler<ChatMessage> {
    private final NetworkService networkService;
    private final CommandBus commandBus;
    private final Map<String, ChatCommand> commands = new HashMap<>();

    public ChatMessageHandler(final NetworkService service, final CommandBus bus) {
        this(service, bus, java.util.List.of(new SelectChatCommand()));
    }

    public ChatMessageHandler(final NetworkService service,
                              final CommandBus bus,
                              final Iterable<ChatCommand> chatCommands) {
        super(ChatMessage.class);
        this.networkService = service;
        this.commandBus = bus;
        for (ChatCommand cmd : chatCommands) {
            commands.put(cmd.name(), cmd);
        }
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
        if (parts.length == 0) {
            return;
        }
        ChatCommand cmd = commands.get(parts[0]);
        if (cmd != null) {
            cmd.execute(parts, commandBus);
        }
    }
}
