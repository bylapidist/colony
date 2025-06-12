package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.components.maps.TileComponent;
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
    public void tracksSelectedTileIndices() {
        MapState state = new MapState();
        state.tiles().put(new TilePos(0, 0), TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true).selected(true)
                .build());
        state.tiles().put(new TilePos(1, 0), TileData.builder()
                .x(1).y(0).tileType("GRASS").passable(true)
                .build());

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapInitSystem(new ProvidedMapStateProvider(state)),
                        new MapRenderDataSystem())
                .build());
        world.process();

        MapRenderDataSystem system = world.getSystem(MapRenderDataSystem.class);
        assertEquals(1, system.getSelectedTileIndices().size);
        assertEquals(0, system.getSelectedTileIndices().first());

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
        var tc = world.getMapper(net.lapidist.colony.components.maps.TileComponent.class)
                .get(tile);
        tc.setSelected(true);
        tc.setDirty(true);
        system.addDirtyIndex(0);
        map.incrementVersion();
        world.process();
        assertSame(firstData, system.getRenderData());
        assertTrue(system.getRenderData().getTiles().first().isSelected());
        assertFalse(tc.isDirty());
        assertEquals(1, system.getSelectedTileIndices().size);
        assertEquals(0, system.getSelectedTileIndices().first());
        world.dispose();
    }


    @Test
    public void onlyChangedTilesAreReplaced() {
        MapState state = new MapState();
        state.tiles().put(new TilePos(0, 0), TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());
        state.tiles().put(new TilePos(1, 0), TileData.builder()
                .x(1).y(0).tileType("GRASS").passable(true)
                .build());

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapInitSystem(new ProvidedMapStateProvider(state)),
                        new MapRenderDataSystem())
                .build());
        world.process();

        MapRenderDataSystem system = world.getSystem(MapRenderDataSystem.class);
        var data = (net.lapidist.colony.client.render.SimpleMapRenderData) system.getRenderData();
        var firstTile = data.getTiles().get(0);
        var secondTile = data.getTiles().get(1);

        var map = net.lapidist.colony.map.MapUtils.findMap(world).orElseThrow();
        var entity = map.getTiles().first();
        var tc = world.getMapper(net.lapidist.colony.components.maps.TileComponent.class)
                .get(entity);
        tc.setSelected(true);
        tc.setDirty(true);
        system.addDirtyIndex(0);
        map.incrementVersion();

        world.process();

        var updated = (net.lapidist.colony.client.render.SimpleMapRenderData) system.getRenderData();
        assertNotSame(firstTile, updated.getTiles().get(0));
        assertSame(secondTile, updated.getTiles().get(1));
        assertTrue(updated.getTiles().get(0).isSelected());
        assertFalse(tc.isDirty());
        world.dispose();
    }

    @Test
    public void exposesUpdatedIndices() {
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
        var map = net.lapidist.colony.map.MapUtils.findMap(world).orElseThrow();
        var tile = map.getTiles().first();
        var tc = world.getMapper(TileComponent.class).get(tile);
        tc.setSelected(true);
        tc.setDirty(true);
        system.addDirtyIndex(0);
        map.incrementVersion();

        world.process();

        var indices = system.consumeUpdatedIndices();
        assertEquals(1, indices.size);
        assertEquals(0, indices.first());
        assertEquals(0, system.consumeUpdatedIndices().size);
        world.dispose();
    }
}
