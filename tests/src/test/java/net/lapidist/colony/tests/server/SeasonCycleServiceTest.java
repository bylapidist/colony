package net.lapidist.colony.tests.server;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.Season;
import net.lapidist.colony.server.services.SeasonCycleService;
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
        SeasonCycleService service = new SeasonCycleService(
                INTERVAL,
                SEASON_LENGTH,
                ref::get,
                ref::set,
                new ReentrantLock()
        );

        service.start();
        Thread.sleep(WAIT_MS);
        service.stop();

        assertEquals(Season.SUMMER, ref.get().environment().season());
    }
}
