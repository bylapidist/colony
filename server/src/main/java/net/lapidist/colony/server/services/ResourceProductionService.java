package net.lapidist.colony.server.services;

import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.registry.ResourceDefinition;
import net.lapidist.colony.registry.BuildingDefinition;

import net.lapidist.colony.mod.ScheduledService;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Periodically increases player food based on existing farms.
 */
public final class ResourceProductionService extends ScheduledService {
    private final Supplier<MapState> supplier;
    private final Consumer<MapState> consumer;
    private final NetworkService networkService;
    private final ReentrantLock lock;
    private final BuildingDefinition farmDef;

    public ResourceProductionService(
            final long intervalToUse,
            final Supplier<MapState> stateSupplier,
            final Consumer<MapState> stateConsumer,
            final NetworkService networkServiceToUse,
            final ReentrantLock lockToUse
    ) {
        super(intervalToUse);
        this.supplier = stateSupplier;
        this.consumer = stateConsumer;
        this.networkService = networkServiceToUse;
        this.lock = lockToUse;
        this.farmDef = Registries.buildings().get("farm");
    }


    @Override
    protected void runTask() {
        MapState state;
        lock.lock();
        try {
            state = supplier.get();
            if (farmDef == null) {
                return;
            }
            long farms = state.buildings().stream()
                    .map(BuildingData::buildingType)
                    .filter(t -> farmDef.id().equalsIgnoreCase(t))
                    .count();
            if (farms == 0) {
                return;
            }
            ResourceDefinition food = Registries.resources().get("FOOD");
            if (food == null) {
                return;
            }
            ResourceData player = state.playerResources();
            java.util.Map<String, Integer> amounts = new java.util.HashMap<>(player.amounts());
            amounts.merge(food.id(), (int) farms, Integer::sum);
            ResourceData updated = new ResourceData(new java.util.HashMap<>(amounts));
            MapState newState = state.toBuilder()
                    .playerResources(updated)
                    .build();
            consumer.accept(newState);
            networkService.broadcast(new ResourceUpdateData(
                    -1,
                    -1,
                    new java.util.HashMap<>(updated.amounts())
            ));
        } finally {
            lock.unlock();
        }
    }
}
