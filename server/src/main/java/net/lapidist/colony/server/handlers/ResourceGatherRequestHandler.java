package net.lapidist.colony.server.handlers;

import net.lapidist.colony.components.state.ResourceGatherRequestData;
import net.lapidist.colony.server.commands.CommandBus;
import net.lapidist.colony.server.commands.GatherCommand;
import net.lapidist.colony.server.commands.ServerCommand;

/**
 * Converts incoming {@link ResourceGatherRequestData} into {@link GatherCommand}.
 *
 * Client system: {@code net.lapidist.colony.client.systems.network.ResourceUpdateSystem}
 */
public final class ResourceGatherRequestHandler
        extends CommandBusMessageHandler<ResourceGatherRequestData> {

    public ResourceGatherRequestHandler(final CommandBus bus) {
        super(ResourceGatherRequestData.class, bus);
    }

    @Override
    protected ServerCommand convert(final ResourceGatherRequestData data) {
        return new GatherCommand(data.x(), data.y(), data.resourceId());
    }
}
