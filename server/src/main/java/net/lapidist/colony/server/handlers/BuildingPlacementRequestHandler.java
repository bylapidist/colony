package net.lapidist.colony.server.handlers;

import net.lapidist.colony.components.state.messages.BuildingPlacementData;
import net.lapidist.colony.server.commands.BuildCommand;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.ServerCommand;

/**
 * Converts incoming {@link BuildingPlacementData} messages into {@link BuildCommand} instances.
 *
 * Client system: {@code net.lapidist.colony.client.systems.network.BuildingUpdateSystem}
 */
public final class BuildingPlacementRequestHandler
        extends CommandBusMessageHandler<BuildingPlacementData> {

    public BuildingPlacementRequestHandler(final CommandBus bus) {
        super(BuildingPlacementData.class, bus);
    }

    @Override
    protected ServerCommand convert(final BuildingPlacementData data) {
        return new BuildCommand(data.x(), data.y(), data.buildingId());
    }
}
