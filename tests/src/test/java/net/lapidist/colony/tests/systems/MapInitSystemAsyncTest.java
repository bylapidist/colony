package net.lapidist.colony.tests.systems;

import com.artemis.Aspect;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.IntBag;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.map.ChunkedMapGenerator;
import net.lapidist.colony.map.GeneratedMapStateProvider;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class MapInitSystemAsyncTest {
    private static final int WAIT_MS = 5000;
    @Test
    public void generatesMapInBackground() throws Exception {
        GeneratedMapStateProvider provider = new GeneratedMapStateProvider(new ChunkedMapGenerator(), 2, 2);
        AtomicReference<Float> progress = new AtomicReference<>(0f);
        MapInitSystem system = new MapInitSystem(provider, progress::set);
        World world = new World(new WorldConfigurationBuilder().with(system).build());
        world.process();
        long end = System.currentTimeMillis() + WAIT_MS;
        while (!system.isReady() && System.currentTimeMillis() < end) {
            Thread.sleep(1);
            world.process();
        }
        assertTrue(system.isReady());
        assertEquals(1f, progress.get(), 0f);
        IntBag maps = world.getAspectSubscriptionManager().get(Aspect.all(MapComponent.class)).getEntities();
        assertEquals(1, maps.size());
    }
}
