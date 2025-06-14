package net.lapidist.colony.tests.server;

import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.server.services.NetworkService;
import net.lapidist.colony.server.services.ResourceProductionService;
import org.junit.Test;
import java.util.concurrent.locks.ReentrantLock;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ResourceProductionServiceTest {

    private static final int INTERVAL = 10;
    private static final int WAIT_MS = 50;

    @Test
    public void increasesFoodWhenFarmsExist() throws Exception {
        MapState state = new MapState();
        state.buildings().add(new BuildingData(0, 0, "farm"));
        state = state.toBuilder().playerResources(new ResourceData()).build();
        AtomicReference<MapState> ref = new AtomicReference<>(state);
        NetworkService network = mock(NetworkService.class);
        ResourceProductionService service = new ResourceProductionService(
                INTERVAL,
                ref::get,
                ref::set,
                network,
                new ReentrantLock()
        );

        service.start();
        Thread.sleep(WAIT_MS);
        service.stop();

        assertTrue(ref.get().playerResources().food() > 0);
        verify(network, atLeastOnce()).broadcast(isA(ResourceUpdateData.class));
    }
}
