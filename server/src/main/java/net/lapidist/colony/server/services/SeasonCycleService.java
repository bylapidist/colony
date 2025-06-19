package net.lapidist.colony.server.services;

import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.Season;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** Service that periodically advances the world season. */
public final class SeasonCycleService extends EnvironmentCycleService {

    public SeasonCycleService(final long periodMs,
                              final float lengthInSeconds,
                              final Supplier<MapState> stateSupplier,
                              final Consumer<MapState> stateConsumer,
                              final ReentrantLock stateLock) {
        super(periodMs, stateSupplier, stateConsumer, stateLock,
                new SeasonUpdater(lengthInSeconds));
    }

    private static final class SeasonUpdater implements BiFunction<EnvironmentState, Float, EnvironmentState> {
        private final float seasonLength;
        private float elapsed;

        SeasonUpdater(final float lengthInSeconds) {
            this.seasonLength = lengthInSeconds;
        }

        @Override
        public EnvironmentState apply(final EnvironmentState env, final Float deltaSeconds) {
            elapsed += deltaSeconds;
            if (elapsed < seasonLength) {
                return env;
            }
            elapsed -= seasonLength;
            Season next = env.season().next();
            return new EnvironmentState(env.timeOfDay(), next, env.moonPhase());
        }
    }
}
