package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.input.TileSelectionHandler;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
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
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapInitSystem(new ProvidedMapStateProvider(state)),
                        new MapRenderDataSystem())
                .build());
        world.process();

        MapRenderDataSystem system = world.getSystem(MapRenderDataSystem.class);
        assertNotNull(system.getRenderData());
        int expected = state.width() * state.height();
        assertEquals(expected, system.getRenderData().getTiles().size);
        assertEquals(0, system.getRenderData().getBuildings().size);

        world.dispose();
    }

    @Test
    public void tracksSelectedTileIndices() {
        MapState state = new MapState();
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true).selected(true)
                .build());
        state.putTile(TileData.builder()
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
        state.putTile(TileData.builder()
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
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());
        state.putTile(TileData.builder()
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
        state.putTile(TileData.builder()
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

    @Test
    public void selectionUpdatesDirtyIndices() {
        MapState state = new MapState();
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapInitSystem(new ProvidedMapStateProvider(state)),
                        new MapRenderDataSystem(),
                        new PlayerCameraSystem())
                .build());
        world.process();

        MapRenderDataSystem system = world.getSystem(MapRenderDataSystem.class);
        PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
        ((com.badlogic.gdx.graphics.OrthographicCamera) camera.getCamera()).position.set(
                net.lapidist.colony.components.GameConstants.TILE_SIZE / 2f,
                net.lapidist.colony.components.GameConstants.TILE_SIZE / 2f,
                0
        );
        camera.getCamera().update();

        TileSelectionHandler handler = new TileSelectionHandler(
                org.mockito.Mockito.mock(net.lapidist.colony.client.network.GameClient.class),
                camera,
                new com.badlogic.gdx.utils.Array<>(),
                system
        );

        var map = net.lapidist.colony.map.MapUtils.findMap(world).orElseThrow();
        var tileMapper = world.getMapper(TileComponent.class);
        com.badlogic.gdx.math.Vector2 screen = net.lapidist.colony.client.graphics.CameraUtils.worldToScreenCoords(
                camera.getViewport(), 0, 0);
        handler.handleTap(screen.x, screen.y, map, tileMapper);
        world.process();
        assertEquals(1, system.getSelectedTileIndices().size);
        assertEquals(0, system.getSelectedTileIndices().first());
        world.dispose();
    }
}
