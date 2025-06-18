package net.lapidist.colony.server.handlers;

import net.lapidist.colony.components.state.messages.BuildingRemovalData;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.RemoveBuildingCommand;
import net.lapidist.colony.server.commands.ServerCommand;

/**
 * Converts incoming {@link BuildingRemovalData} messages into {@link RemoveBuildingCommand} instances.
 *
 * Client system: {@code net.lapidist.colony.client.systems.network.BuildingUpdateSystem}
 */
public final class BuildingRemovalRequestHandler
        extends CommandBusMessageHandler<BuildingRemovalData> {

    public BuildingRemovalRequestHandler(final CommandBus bus) {
        super(BuildingRemovalData.class, bus);
    }

    @Override
    protected ServerCommand convert(final BuildingRemovalData data) {
        return new RemoveBuildingCommand(data.x(), data.y());
    }
}
