package net.lapidist.colony.server.handlers;

import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.TileSelectionCommand;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.network.AbstractMessageHandler;

/**
 * Converts incoming {@link TileSelectionData} messages into
 * {@link TileSelectionCommand} instances dispatched on the command bus.
 *
 * Client system: {@code net.lapidist.colony.client.systems.network.TileUpdateSystem}
 */
public final class TileSelectionRequestHandler extends AbstractMessageHandler<TileSelectionData> {
    private final CommandBus commandBus;

    public TileSelectionRequestHandler(final CommandBus bus) {
        super(TileSelectionData.class);
        this.commandBus = bus;
    }

    @Override
    public void handle(final TileSelectionData data) {
        commandBus.dispatch(new TileSelectionCommand(data.x(), data.y(), data.selected()));
    }
}
