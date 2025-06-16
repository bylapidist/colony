package net.lapidist.colony.server.services;

import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.Season;
import net.lapidist.colony.mod.GameSystem;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** Service that periodically advances the world season. */
public final class SeasonCycleService implements GameSystem {
    private final long period;
    private final float seasonLength;
    private final Supplier<MapState> supplier;
    private final Consumer<MapState> consumer;
    private final ReentrantLock lock;
    private ScheduledExecutorService executor;
    private float elapsed;

    public SeasonCycleService(final long periodMs,
                              final float lengthInSeconds,
                              final Supplier<MapState> stateSupplier,
                              final Consumer<MapState> stateConsumer,
                              final ReentrantLock stateLock) {
        this.period = periodMs;
        this.seasonLength = lengthInSeconds;
        this.supplier = stateSupplier;
        this.consumer = stateConsumer;
        this.lock = stateLock;
    }

    @Override
    public void start() {
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        executor.scheduleAtFixedRate(this::tick, period, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    private void tick() {
        lock.lock();
        try {
            elapsed += period / 1000f;
            if (elapsed < seasonLength) {
                return;
            }
            elapsed -= seasonLength;
            MapState state = supplier.get();
            EnvironmentState env = state.environment();
            Season next = env.season().next();
            consumer.accept(state.toBuilder()
                    .environment(new EnvironmentState(env.timeOfDay(), next, env.moonPhase()))
                    .build());
        } finally {
            lock.unlock();
        }
    }
}
