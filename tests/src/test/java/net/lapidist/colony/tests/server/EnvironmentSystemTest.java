package net.lapidist.colony.tests.server;

import net.lapidist.colony.components.state.EnvironmentUpdate;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.server.systems.EnvironmentSystem;
import net.lapidist.colony.server.GameServer;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class EnvironmentSystemTest {
    private static final int WAIT_MS = 60;

    @Test
    public void advancesEnvironmentAndBroadcasts() throws Exception {
        MapState state = new MapState();
        AtomicReference<MapState> ref = new AtomicReference<>(state);
        GameServer server = mock(GameServer.class);
        when(server.getMapState()).thenAnswer(inv -> ref.get());
        doAnswer(inv -> {
            ref.set((MapState) inv.getArguments()[0]);
            return null;
        }).when(server).setMapState(any());
        when(server.getStateLock()).thenReturn(new ReentrantLock());

        EnvironmentSystem system = new EnvironmentSystem(server);
        system.start();
        Thread.sleep(WAIT_MS);
        system.stop();

        verify(server, atLeastOnce()).broadcast(isA(EnvironmentUpdate.class));
        assertNotNull(ref.get().environment());
    }
}
