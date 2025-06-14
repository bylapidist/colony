package net.lapidist.colony.server.services;

import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.ResourceUpdateData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Periodically increases player food based on existing farms.
 */
public final class ResourceProductionService {

    private final long interval;
    private final Supplier<MapState> supplier;
    private final Consumer<MapState> consumer;
    private final NetworkService networkService;
    private final ReentrantLock lock;
    private ScheduledExecutorService executor;

    public ResourceProductionService(
            final long intervalToUse,
            final Supplier<MapState> stateSupplier,
            final Consumer<MapState> stateConsumer,
            final NetworkService networkServiceToUse,
            final ReentrantLock lockToUse
    ) {
        this.interval = intervalToUse;
        this.supplier = stateSupplier;
        this.consumer = stateConsumer;
        this.networkService = networkServiceToUse;
        this.lock = lockToUse;
    }

    /** Starts periodic production on a daemon thread. */
    public void start() {
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        executor.scheduleAtFixedRate(this::produce, interval, interval, TimeUnit.MILLISECONDS);
    }

    /** Stops the scheduler. */
    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    private void produce() {
        MapState state;
        lock.lock();
        try {
            state = supplier.get();
            long farms = state.buildings().stream()
                    .map(BuildingData::buildingType)
                    .filter(t -> "farm".equalsIgnoreCase(t))
                    .count();
            if (farms == 0) {
                return;
            }
            ResourceData player = state.playerResources();
            ResourceData updated = new ResourceData(
                    player.wood(),
                    player.stone(),
                    player.food() + (int) farms
            );
            MapState newState = state.toBuilder()
                    .playerResources(updated)
                    .build();
            consumer.accept(newState);
            networkService.broadcast(new ResourceUpdateData(
                    -1,
                    -1,
                    updated.wood(),
                    updated.stone(),
                    updated.food()
            ));
        } finally {
            lock.unlock();
        }
    }
}
