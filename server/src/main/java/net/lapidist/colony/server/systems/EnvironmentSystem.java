package net.lapidist.colony.server.systems;

import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.EnvironmentUpdate;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.Season;
import net.lapidist.colony.mod.GameSystem;
import net.lapidist.colony.server.GameServer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** Periodically advances the world environment and broadcasts updates. */
public final class EnvironmentSystem implements GameSystem {
    private static final int PERIOD_MS = 50;
    private static final int DAYS_PER_SEASON = 30;
    private static final float HOURS_PER_DAY = 24f;
    private static final float MILLIS_IN_SECOND = 1000f;
    private static final float SPRING_LENGTH = 14f;
    private static final float SUMMER_LENGTH = 16f;
    private static final float AUTUMN_LENGTH = 12f;
    private static final float WINTER_LENGTH = 8f;

    private final GameServer server;
    private ScheduledExecutorService executor;
    private int dayInSeason;

    public EnvironmentSystem(final GameServer srv) {
        this.server = srv;
    }

    @Override
    public void start() {
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        executor.scheduleAtFixedRate(this::tick, PERIOD_MS, PERIOD_MS, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    private void tick() {
        MapState state = server.getMapState();
        EnvironmentState env = state.environment();
        float dayLength = dayLength(env.season());
        float increment = (PERIOD_MS / MILLIS_IN_SECOND) * HOURS_PER_DAY / dayLength;
        float time = env.timeOfDay() + increment;
        Season season = env.season();
        float moon = env.moonPhase();
        if (time >= HOURS_PER_DAY) {
            time -= HOURS_PER_DAY;
            dayInSeason++;
            moon += 1f / DAYS_PER_SEASON;
            if (moon >= 1f) {
                moon -= 1f;
            }
            if (dayInSeason >= DAYS_PER_SEASON) {
                dayInSeason = 0;
                season = next(season);
            }
        }
        EnvironmentState updated = new EnvironmentState(time, season, moon);
        server.setMapState(state.toBuilder().environment(updated).build());
        server.broadcast(new EnvironmentUpdate(updated));
    }

    private static Season next(final Season current) {
        Season[] values = Season.values();
        return values[(current.ordinal() + 1) % values.length];
    }

    private static float dayLength(final Season season) {
        return switch (season) {
            case SPRING -> SPRING_LENGTH;
            case SUMMER -> SUMMER_LENGTH;
            case AUTUMN -> AUTUMN_LENGTH;
            case WINTER -> WINTER_LENGTH;
        };
    }
}
