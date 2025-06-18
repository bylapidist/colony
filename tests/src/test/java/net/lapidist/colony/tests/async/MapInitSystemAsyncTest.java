package net.lapidist.colony.tests.async;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.map.TileData;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MapInitSystemAsyncTest {

    private static final int WAIT_MS = 1000;
    private static final int SLEEP_MS = 10;

    @Test
    public void generatesMapInBackground() throws Exception {
        new net.lapidist.colony.base.BaseDefinitionsMod().init();
        MapState state = new MapState();
        state.putTile(TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .passable(true)
                .build());
        AtomicBoolean done = new AtomicBoolean(false);
        MapInitSystem init = new MapInitSystem(new ProvidedMapStateProvider(state), true, p -> {
            if (p == 1f) {
                done.set(true);
            }
        });
        World world = new World(new WorldConfigurationBuilder().with(init).build());
        long wait = System.currentTimeMillis() + WAIT_MS;
        while (!init.isReady() && System.currentTimeMillis() < wait) {
            world.process();
            Thread.sleep(SLEEP_MS);
        }
        assertTrue(done.get());
        world.process();
        var maps = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(MapComponent.class))
                .getEntities();
        assertEquals(1, maps.size());
    }
}
