package net.lapidist.colony.server.handlers;

import net.lapidist.colony.components.state.BuildingPlacementData;
import net.lapidist.colony.server.commands.BuildCommand;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.network.AbstractMessageHandler;

/**
 * Converts incoming {@link BuildingPlacementData} messages into {@link BuildCommand} instances.
 */
public final class BuildingPlacementRequestHandler extends AbstractMessageHandler<BuildingPlacementData> {
    private final CommandBus commandBus;

    public BuildingPlacementRequestHandler(final CommandBus bus) {
        super(BuildingPlacementData.class);
        this.commandBus = bus;
    }

    @Override
    public void handle(final BuildingPlacementData data) {
        commandBus.dispatch(new BuildCommand(
                data.x(),
                data.y(),
                data.buildingType()
        ));
    }
}
