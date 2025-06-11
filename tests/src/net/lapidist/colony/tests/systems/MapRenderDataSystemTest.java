package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapRenderDataSystemTest {

    @Test
    public void returnsNullWhenNoMapPresent() {
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapRenderDataSystem())
                .build());
        world.process();

        MapRenderDataSystem system = world.getSystem(MapRenderDataSystem.class);
        assertNull(system.getRenderData());
    }

    @Test
    public void populatesRenderDataFromMap() {
        MapState state = new MapState();
        state.tiles().put(new TilePos(0, 0), TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapInitSystem(new ProvidedMapStateProvider(state)),
                        new MapRenderDataSystem())
                .build());
        world.process();

        MapRenderDataSystem system = world.getSystem(MapRenderDataSystem.class);
        assertNotNull(system.getRenderData());
        assertEquals(1, system.getRenderData().getTiles().size);

        world.dispose();
    }
}
