package net.lapidist.colony.server.commands;

import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.server.events.BuildingPlacedEvent;
import net.lapidist.colony.server.services.NetworkService;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.registry.BuildingDefinition;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Applies a {@link BuildCommand} to the game state and broadcasts the change.
 */
public final class BuildCommandHandler implements CommandHandler<BuildCommand> {
    private static final Map<String, ResourceData> COSTS = Map.of(
            "house", new ResourceData(1, 0, 0),
            "market", new ResourceData(5, 2, 0),
            "factory", new ResourceData(10, 5, 0),
            "farm", new ResourceData(2, 0, 0)
    );

    private final Supplier<MapState> stateSupplier;
    private final Consumer<MapState> stateConsumer;
    private final NetworkService networkService;
    private final ReentrantLock lock;

    public BuildCommandHandler(final Supplier<MapState> stateSupplierToUse,
                               final Consumer<MapState> stateConsumerToUse,
                               final NetworkService networkServiceToUse,
                               final ReentrantLock lockToUse) {
        this.stateSupplier = stateSupplierToUse;
        this.stateConsumer = stateConsumerToUse;
        this.networkService = networkServiceToUse;
        this.lock = lockToUse;
    }

    @Override
    public Class<BuildCommand> type() {
        return BuildCommand.class;
    }

    @Override
    public void handle(final BuildCommand command) {
        lock.lock();
        try {
            MapState state = stateSupplier.get();
            TileData tile = state.getTile(command.x(), command.y());
            boolean occupied = state.buildings().stream()
                    .anyMatch(b -> b.x() == command.x() && b.y() == command.y());
            if (tile == null || occupied) {
                return;
            }
            BuildingDefinition def = Registries.buildings().get(command.buildingId());
            if (def == null) {
                return;
            }
            String type = def.id();
            ResourceData cost = COSTS.getOrDefault(type, new ResourceData());
            ResourceData player = state.playerResources();
            if (player.wood() < cost.wood()
                    || player.stone() < cost.stone()
                    || player.food() < cost.food()) {
                return;
            }
            BuildingData building = new BuildingData(command.x(), command.y(), type);
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
            Events.dispatch(new BuildingPlacedEvent(command.x(), command.y(), type));
            networkService.broadcast(building);
            networkService.broadcast(new ResourceUpdateData(-1, -1,
                    new java.util.HashMap<>(newResources.amounts())));
        } finally {
            lock.unlock();
        }
    }

}
