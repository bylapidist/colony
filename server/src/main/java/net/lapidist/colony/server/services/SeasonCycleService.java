package net.lapidist.colony.server.services;

import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.Season;
import net.lapidist.colony.mod.ScheduledService;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** Service that periodically advances the world season. */
public final class SeasonCycleService extends ScheduledService {
    private static final float MS_TO_SECONDS = 1000f;
    private final long period;
    private final float seasonLength;
    private final Supplier<MapState> supplier;
    private final Consumer<MapState> consumer;
    private final ReentrantLock lock;
    private float elapsed;

    public SeasonCycleService(final long periodMs,
                              final float lengthInSeconds,
                              final Supplier<MapState> stateSupplier,
                              final Consumer<MapState> stateConsumer,
                              final ReentrantLock stateLock) {
        super(periodMs);
        this.period = periodMs;
        this.seasonLength = lengthInSeconds;
        this.supplier = stateSupplier;
        this.consumer = stateConsumer;
        this.lock = stateLock;
    }



    @Override
    protected void runTask() {
        lock.lock();
        try {
            elapsed += period / MS_TO_SECONDS;
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
