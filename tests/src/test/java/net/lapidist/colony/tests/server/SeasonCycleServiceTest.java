package net.lapidist.colony.tests.server;

import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.Season;
import net.lapidist.colony.server.services.EnvironmentCycleService;
import net.lapidist.colony.components.state.EnvironmentState;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

public class SeasonCycleServiceTest {
    private static final int INTERVAL = 10;
    private static final int WAIT_MS = 30;
    private static final float SEASON_LENGTH = 0.02f;

    @Test
    public void advancesSeason() throws Exception {
        MapState state = new MapState();
        AtomicReference<MapState> ref = new AtomicReference<>(state);
        EnvironmentCycleService service = new EnvironmentCycleService(
                INTERVAL,
                ref::get,
                ref::set,
                new ReentrantLock(),
                new java.util.function.BiFunction<EnvironmentState, Float, EnvironmentState>() {
                    private float elapsed;

                    @Override
                    public EnvironmentState apply(final EnvironmentState env, final Float dt) {
                        elapsed += dt;
                        if (elapsed < SEASON_LENGTH) {
                            return env;
                        }
                        elapsed -= SEASON_LENGTH;
                        return new EnvironmentState(env.timeOfDay(), env.season().next(), env.moonPhase());
                    }
                }
        );

        service.start();
        Thread.sleep(WAIT_MS);
        service.stop();

        assertEquals(Season.SUMMER, ref.get().environment().season());
    }
}
