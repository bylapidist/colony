package net.lapidist.colony.server.services;

import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.map.MapState;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Periodically advances the time of day stored in the map state.
 */
public final class DayNightCycleService extends EnvironmentCycleService {
    private static final float HOURS_PER_DAY = 24f;

    public DayNightCycleService(
            final long periodMs,
            final float dayLengthInSeconds,
            final Supplier<MapState> stateSupplier,
            final Consumer<MapState> stateConsumer,
            final ReentrantLock stateLock) {
        super(periodMs, stateSupplier, stateConsumer, stateLock,
                new DayNightUpdater(dayLengthInSeconds));
    }

    private static final class DayNightUpdater implements BiFunction<EnvironmentState, Float, EnvironmentState> {
        private final float dayLength;

        DayNightUpdater(final float dayLengthInSeconds) {
            this.dayLength = dayLengthInSeconds;
        }

        @Override
        public EnvironmentState apply(final EnvironmentState env, final Float intervalSeconds) {
            float increment = (intervalSeconds * HOURS_PER_DAY) / dayLength;
            float time = wrap(env.timeOfDay() + increment);
            return new EnvironmentState(time, env.season(), env.moonPhase());
        }
    }

    private static float wrap(final float time) {
        float t = time % HOURS_PER_DAY;
        if (t < 0f) {
            t += HOURS_PER_DAY;
        }
        return t;
    }
}
