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
        assertEquals(0, system.getRenderData().getBuildings().size);

        world.dispose();
    }

    @Test
    public void updatesWhenMapChanges() {
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
        var firstData = system.getRenderData();
        assertFalse(firstData.getTiles().first().isSelected());
        var map = net.lapidist.colony.map.MapUtils.findMap(world).orElseThrow();
        var tile = map.getTiles().first();
        world.getMapper(net.lapidist.colony.components.maps.TileComponent.class)
                .get(tile).setSelected(true);
        map.incrementVersion();
        world.process();
        assertSame(firstData, system.getRenderData());
        assertTrue(system.getRenderData().getTiles().first().isSelected());
        world.dispose();
    }
}
