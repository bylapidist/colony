package net.lapidist.colony.tests.server;

import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.server.services.EnvironmentCycleService;
import net.lapidist.colony.components.state.EnvironmentState;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertTrue;

public class DayNightCycleServiceTest {
    private static final int INTERVAL = 10;
    private static final float HOURS_PER_DAY = 24f;
    private static final float DAY_LENGTH = 0.02f;

    @Test
    public void advancesTimeOfDay() throws Exception {
        MapState state = new MapState();
        AtomicReference<MapState> ref = new AtomicReference<>(state);
        EnvironmentCycleService service = new EnvironmentCycleService(
                INTERVAL,
                ref::get,
                ref::set,
                new ReentrantLock(),
                (env, dt) -> {
                    float inc = (dt * HOURS_PER_DAY) / DAY_LENGTH;
                    float time = ((env.timeOfDay() + inc) % HOURS_PER_DAY + HOURS_PER_DAY) % HOURS_PER_DAY;
                    return new EnvironmentState(time, env.season(), env.moonPhase());
                }
        );

        java.lang.reflect.Method run = EnvironmentCycleService.class.getDeclaredMethod("runTask");
        run.setAccessible(true);
        run.invoke(service);
        assertTrue(ref.get().environment().timeOfDay() > 0f);
    }
}
