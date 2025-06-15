package net.lapidist.colony.server.handlers;

import net.lapidist.colony.components.state.BuildingRemovalData;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.RemoveBuildingCommand;
import net.lapidist.colony.network.AbstractMessageHandler;

/**
 * Converts incoming {@link BuildingRemovalData} messages into {@link RemoveBuildingCommand} instances.
 *
 * Client system: {@code net.lapidist.colony.client.systems.network.BuildingUpdateSystem}
 */
public final class BuildingRemovalRequestHandler extends AbstractMessageHandler<BuildingRemovalData> {
    private final CommandBus commandBus;

    public BuildingRemovalRequestHandler(final CommandBus bus) {
        super(BuildingRemovalData.class);
        this.commandBus = bus;
    }

    @Override
    public void handle(final BuildingRemovalData data) {
        commandBus.dispatch(new RemoveBuildingCommand(data.x(), data.y()));
    }
}
