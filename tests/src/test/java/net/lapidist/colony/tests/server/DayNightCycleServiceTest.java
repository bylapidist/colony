package net.lapidist.colony.tests.server;

import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.server.services.DayNightCycleService;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertTrue;

public class DayNightCycleServiceTest {
    private static final int INTERVAL = 10;
    private static final float DAY_LENGTH = 0.02f;

    @Test
    public void advancesTimeOfDay() throws Exception {
        MapState state = new MapState();
        AtomicReference<MapState> ref = new AtomicReference<>(state);
        DayNightCycleService service = new DayNightCycleService(
                INTERVAL,
                DAY_LENGTH,
                ref::get,
                ref::set,
                new ReentrantLock()
        );

        java.lang.reflect.Method run = DayNightCycleService.class.getDeclaredMethod("runTask");
        run.setAccessible(true);
        run.invoke(service);
        assertTrue(ref.get().environment().timeOfDay() > 0f);
    }
}
