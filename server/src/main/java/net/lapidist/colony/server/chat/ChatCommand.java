package net.lapidist.colony.server.chat;

import net.lapidist.colony.server.commands.CommandBus;

/**
 * Represents a chat command that can be executed by the server.
 */
public interface ChatCommand {
    /**
     * @return the textual name that triggers this command
     */
    String name();

    /**
     * Execute the command using the given arguments.
     *
     * @param args full argument array including the command name at index 0
     * @param commandBus the server command bus to dispatch resulting commands
     */
    void execute(String[] args, CommandBus commandBus);
}
