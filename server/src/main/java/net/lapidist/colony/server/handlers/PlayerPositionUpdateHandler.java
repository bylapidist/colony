package net.lapidist.colony.server.handlers;

import net.lapidist.colony.components.state.messages.PlayerPositionUpdate;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.PlayerPositionCommand;
import net.lapidist.colony.server.commands.ServerCommand;

/** Converts incoming {@link PlayerPositionUpdate} into {@link PlayerPositionCommand}. */
public final class PlayerPositionUpdateHandler
        extends CommandBusMessageHandler<PlayerPositionUpdate> {

    public PlayerPositionUpdateHandler(final CommandBus bus) {
        super(PlayerPositionUpdate.class, bus);
    }

    @Override
    protected ServerCommand convert(final PlayerPositionUpdate message) {
        return new PlayerPositionCommand(message.x(), message.y());
    }
}
