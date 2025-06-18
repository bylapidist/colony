package net.lapidist.colony.server.handlers;

import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.TileSelectionCommand;
import net.lapidist.colony.components.state.messages.TileSelectionData;
import net.lapidist.colony.server.commands.ServerCommand;

/**
 * Converts incoming {@link TileSelectionData} messages into
 * {@link TileSelectionCommand} instances dispatched on the command bus.
 *
 * Client system: {@code net.lapidist.colony.client.systems.network.TileUpdateSystem}
 */
public final class TileSelectionRequestHandler
        extends CommandBusMessageHandler<TileSelectionData> {

    public TileSelectionRequestHandler(final CommandBus bus) {
        super(TileSelectionData.class, bus);
    }

    @Override
    protected ServerCommand convert(final TileSelectionData data) {
        return new TileSelectionCommand(data.x(), data.y(), data.selected());
    }
}
