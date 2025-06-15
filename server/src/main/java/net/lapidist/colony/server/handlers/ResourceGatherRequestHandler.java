package net.lapidist.colony.server.handlers;

import net.lapidist.colony.components.state.ResourceGatherRequestData;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.GatherCommand;
import net.lapidist.colony.network.AbstractMessageHandler;

/** Converts incoming {@link ResourceGatherRequestData} into {@link GatherCommand}. */
public final class ResourceGatherRequestHandler extends AbstractMessageHandler<ResourceGatherRequestData> {
    private final CommandBus commandBus;

    public ResourceGatherRequestHandler(final CommandBus bus) {
        super(ResourceGatherRequestData.class);
        this.commandBus = bus;
    }

    @Override
    public void handle(final ResourceGatherRequestData data) {
        commandBus.dispatch(new GatherCommand(data.x(), data.y(), data.resourceId()));
    }
}
