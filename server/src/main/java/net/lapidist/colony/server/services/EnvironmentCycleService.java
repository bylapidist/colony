package net.lapidist.colony.server.services;

import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.mod.ScheduledService;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Generic service that updates the {@link EnvironmentState} each tick using a supplied function.
 */
public class EnvironmentCycleService extends ScheduledService {
    private static final float MS_TO_SECONDS = 1000f;

    private final long interval;
    private final Supplier<MapState> supplier;
    private final Consumer<MapState> consumer;
    private final ReentrantLock lock;
    private final BiFunction<EnvironmentState, Float, EnvironmentState> updater;

    public EnvironmentCycleService(
            final long intervalMs,
            final Supplier<MapState> stateSupplier,
            final Consumer<MapState> stateConsumer,
            final ReentrantLock stateLock,
            final BiFunction<EnvironmentState, Float, EnvironmentState> updateFn) {
        super(intervalMs);
        this.interval = intervalMs;
        this.supplier = stateSupplier;
        this.consumer = stateConsumer;
        this.lock = stateLock;
        this.updater = updateFn;
    }

    @Override
    protected final void runTask() {
        lock.lock();
        try {
            MapState state = supplier.get();
            EnvironmentState updated = updater.apply(state.environment(), getIntervalSeconds());
            consumer.accept(state.toBuilder().environment(updated).build());
        } finally {
            lock.unlock();
        }
    }

    private float getIntervalSeconds() {
        return interval / MS_TO_SECONDS;
    }
}
