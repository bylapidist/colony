package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.server.events.BuildingPlacedEvent;
import net.lapidist.colony.server.services.NetworkService;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Applies a {@link BuildCommand} to the game state and broadcasts the change.
 */
public final class BuildCommandHandler implements CommandHandler<BuildCommand> {
    private static final Map<String, ResourceData> COSTS = Map.of(
            "HOUSE", new ResourceData(1, 0, 0),
            "MARKET", new ResourceData(5, 2, 0),
            "FACTORY", new ResourceData(10, 5, 0)
    );

    private final Supplier<MapState> stateSupplier;
    private final Consumer<MapState> stateConsumer;
    private final NetworkService networkService;

    public BuildCommandHandler(final Supplier<MapState> stateSupplierToUse,
                               final Consumer<MapState> stateConsumerToUse,
                               final NetworkService networkServiceToUse) {
        this.stateSupplier = stateSupplierToUse;
        this.stateConsumer = stateConsumerToUse;
        this.networkService = networkServiceToUse;
    }

    @Override
    public Class<BuildCommand> type() {
        return BuildCommand.class;
    }

    @Override
    public void handle(final BuildCommand command) {
        MapState state = stateSupplier.get();
        TileData tile = state.tiles().get(new TilePos(command.x(), command.y()));
        boolean occupied = state.buildings().stream()
                .anyMatch(b -> b.x() == command.x() && b.y() == command.y());
        if (tile == null || occupied) {
            return;
        }
        ResourceData cost = COSTS.getOrDefault(command.type(), new ResourceData());
        ResourceData player = state.playerResources();
        if (player.wood() < cost.wood()
                || player.stone() < cost.stone()
                || player.food() < cost.food()) {
            return;
        }
        BuildingData building = new BuildingData(command.x(), command.y(), command.type());
        state.buildings().add(building);
        ResourceData newResources = new ResourceData(
                player.wood() - cost.wood(),
                player.stone() - cost.stone(),
                player.food() - cost.food()
        );
        MapState updated = state.toBuilder()
                .playerResources(newResources)
                .build();
        stateConsumer.accept(updated);
        Events.dispatch(new BuildingPlacedEvent(command.x(), command.y(), command.type()));
        networkService.broadcast(building);
        networkService.broadcast(new ResourceUpdateData(-1, -1,
                newResources.wood(), newResources.stone(), newResources.food()));
    }
}
