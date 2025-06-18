package net.lapidist.colony.server.services;

import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.mod.ScheduledService;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Periodically advances the time of day stored in the map state.
 */
public final class DayNightCycleService extends ScheduledService {
    private static final float HOURS_PER_DAY = 24f;
    private static final float MS_TO_SECONDS = 1000f;
    private final long interval;
    private final float dayLength;
    private final Supplier<MapState> supplier;
    private final Consumer<MapState> consumer;
    private final ReentrantLock lock;

    public DayNightCycleService(
            final long periodMs,
            final float dayLengthInSeconds,
            final Supplier<MapState> stateSupplier,
            final Consumer<MapState> stateConsumer,
            final ReentrantLock stateLock) {
        super(periodMs);
        this.interval = periodMs;
        this.dayLength = dayLengthInSeconds;
        this.supplier = stateSupplier;
        this.consumer = stateConsumer;
        this.lock = stateLock;
    }

    @Override
    protected void runTask() {
        lock.lock();
        try {
            MapState state = supplier.get();
            EnvironmentState env = state.environment();
            float increment = (getIntervalSeconds() * HOURS_PER_DAY) / dayLength;
            float time = wrap(env.timeOfDay() + increment);
            consumer.accept(state.toBuilder()
                    .environment(new EnvironmentState(time, env.season(), env.moonPhase()))
                    .build());
        } finally {
            lock.unlock();
        }
    }

    private float getIntervalSeconds() {
        return interval / MS_TO_SECONDS;
    }

    private static float wrap(final float time) {
        float t = time % HOURS_PER_DAY;
        if (t < 0f) {
            t += HOURS_PER_DAY;
        }
        return t;
    }
}
