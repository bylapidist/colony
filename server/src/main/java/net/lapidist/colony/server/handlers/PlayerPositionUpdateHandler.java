package net.lapidist.colony.server.handlers;

import net.lapidist.colony.components.state.PlayerPositionUpdate;
import net.lapidist.colony.network.AbstractMessageHandler;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.PlayerPositionCommand;

/** Converts incoming {@link PlayerPositionUpdate} into {@link PlayerPositionCommand}. */
public final class PlayerPositionUpdateHandler extends AbstractMessageHandler<PlayerPositionUpdate> {
    private final CommandBus commandBus;

    public PlayerPositionUpdateHandler(final CommandBus bus) {
        super(PlayerPositionUpdate.class);
        this.commandBus = bus;
    }

    @Override
    public void handle(final PlayerPositionUpdate message) {
        commandBus.dispatch(new PlayerPositionCommand(message.x(), message.y()));
    }
}
