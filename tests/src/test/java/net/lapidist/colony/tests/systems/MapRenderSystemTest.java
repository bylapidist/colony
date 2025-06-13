package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.renderers.MapRenderer;
import net.lapidist.colony.client.systems.MapRenderSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import net.lapidist.colony.tests.GdxTestRunner;
import net.lapidist.colony.client.renderers.SpriteBatchMapRenderer;
import com.badlogic.gdx.utils.IntArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class MapRenderSystemTest {

    @Test
    public void findsMapAfterInitSystemRuns() throws Exception {
        GL20 gl = Gdx.gl20;
        Mockito.when(gl.glCreateShader(Mockito.anyInt())).thenReturn(1);
        Mockito.when(gl.glCreateProgram()).thenReturn(1);
        Mockito.doAnswer(inv -> {
            java.nio.IntBuffer buf = (java.nio.IntBuffer) inv.getArguments()[2];
            buf.put(0, 1);
            return null;
        }).when(gl).glGetShaderiv(Mockito.anyInt(), Mockito.anyInt(), Mockito.any());
        Mockito.doAnswer(inv -> {
            java.nio.IntBuffer buf = (java.nio.IntBuffer) inv.getArguments()[2];
            buf.put(0, 1);
            return null;
        }).when(gl).glGetProgramiv(Mockito.anyInt(), Mockito.anyInt(), Mockito.any());
        Mockito.when(gl.glGetShaderInfoLog(Mockito.anyInt())).thenReturn("");
        Mockito.when(gl.glGetProgramInfoLog(Mockito.anyInt())).thenReturn("");
        Mockito.doNothing().when(gl).glShaderSource(Mockito.anyInt(), Mockito.any());
        Mockito.doNothing().when(gl).glCompileShader(Mockito.anyInt());
        Mockito.doNothing().when(gl).glAttachShader(Mockito.anyInt(), Mockito.anyInt());
        Mockito.doNothing().when(gl).glLinkProgram(Mockito.anyInt());
        Mockito.doNothing().when(gl).glDeleteShader(Mockito.anyInt());
        Mockito.doNothing().when(gl).glDeleteProgram(Mockito.anyInt());

        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .passable(true)
                .build();
        state.putTile(tile);

        MapRenderSystem renderSystem = new MapRenderSystem();

        World world = new World(new WorldConfigurationBuilder()
                .with(
                        renderSystem,
                        new MapInitSystem(new ProvidedMapStateProvider(state)),
                        new MapRenderDataSystem(),
                        new PlayerCameraSystem()
                )
                .build());

        renderSystem.setMapRenderer(Mockito.mock(MapRenderer.class));
        renderSystem.setCameraProvider(world.getSystem(PlayerCameraSystem.class));

        world.setDelta(0f);
        world.process();

        MapRenderSystem system = world.getSystem(MapRenderSystem.class);
        Field mapField = MapRenderSystem.class.getDeclaredField("mapData");
        mapField.setAccessible(true);
        Object map = mapField.get(system);

        assertNotNull("MapRenderSystem should find map after initialization", map);
        world.dispose();
    }

    @Test
    public void refreshesMapDataWhenDataSystemUpdates() throws Exception {
        GL20 gl = Gdx.gl20;
        Mockito.when(gl.glCreateShader(Mockito.anyInt())).thenReturn(1);
        Mockito.when(gl.glCreateProgram()).thenReturn(1);
        Mockito.doAnswer(inv -> {
            java.nio.IntBuffer buf = (java.nio.IntBuffer) inv.getArguments()[2];
            buf.put(0, 1);
            return null;
        }).when(gl).glGetShaderiv(Mockito.anyInt(), Mockito.anyInt(), Mockito.any());
        Mockito.doAnswer(inv -> {
            java.nio.IntBuffer buf = (java.nio.IntBuffer) inv.getArguments()[2];
            buf.put(0, 1);
            return null;
        }).when(gl).glGetProgramiv(Mockito.anyInt(), Mockito.anyInt(), Mockito.any());
        Mockito.when(gl.glGetShaderInfoLog(Mockito.anyInt())).thenReturn("");
        Mockito.when(gl.glGetProgramInfoLog(Mockito.anyInt())).thenReturn("");
        Mockito.doNothing().when(gl).glShaderSource(Mockito.anyInt(), Mockito.any());
        Mockito.doNothing().when(gl).glCompileShader(Mockito.anyInt());
        Mockito.doNothing().when(gl).glAttachShader(Mockito.anyInt(), Mockito.anyInt());
        Mockito.doNothing().when(gl).glLinkProgram(Mockito.anyInt());
        Mockito.doNothing().when(gl).glDeleteShader(Mockito.anyInt());
        Mockito.doNothing().when(gl).glDeleteProgram(Mockito.anyInt());

        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .passable(true)
                .build();
        state.putTile(tile);

        MapRenderSystem renderSystem = new MapRenderSystem();
        MapRenderDataSystem dataSystem = new MapRenderDataSystem();

        World world = new World(new WorldConfigurationBuilder()
                .with(
                        renderSystem,
                        new MapInitSystem(new ProvidedMapStateProvider(state)),
                        dataSystem,
                        new PlayerCameraSystem()
                )
                .build());

        renderSystem.setMapRenderer(Mockito.mock(MapRenderer.class));
        renderSystem.setCameraProvider(world.getSystem(PlayerCameraSystem.class));

        world.process();

        MapRenderSystem system = world.getSystem(MapRenderSystem.class);
        Field mapField = MapRenderSystem.class.getDeclaredField("mapData");
        mapField.setAccessible(true);
        MapRenderData first = (MapRenderData) mapField.get(system);
        assertFalse(first.getTiles().first().isSelected());

        var map = net.lapidist.colony.map.MapUtils.findMap(world).orElseThrow();
        var entity = map.getTiles().first();
        var tc = world.getMapper(net.lapidist.colony.components.maps.TileComponent.class)
                .get(entity);
        tc.setSelected(true);
        tc.setDirty(true);
        dataSystem.addDirtyIndex(0);
        map.incrementVersion();

        world.process();
        world.process();

        MapRenderData second = (MapRenderData) mapField.get(system);
        assertSame(first, second);
        assertTrue(second.getTiles().first().isSelected());
        world.dispose();
    }

    @Test
    public void passesInvalidIndicesToRenderer() throws Exception {
        GL20 gl = Gdx.gl20;
        Mockito.when(gl.glCreateShader(Mockito.anyInt())).thenReturn(1);
        Mockito.when(gl.glCreateProgram()).thenReturn(1);
        Mockito.doAnswer(inv -> {
            java.nio.IntBuffer buf = (java.nio.IntBuffer) inv.getArguments()[2];
            buf.put(0, 1);
            return null;
        }).when(gl).glGetShaderiv(Mockito.anyInt(), Mockito.anyInt(), Mockito.any());
        Mockito.doAnswer(inv -> {
            java.nio.IntBuffer buf = (java.nio.IntBuffer) inv.getArguments()[2];
            buf.put(0, 1);
            return null;
        }).when(gl).glGetProgramiv(Mockito.anyInt(), Mockito.anyInt(), Mockito.any());
        Mockito.when(gl.glGetShaderInfoLog(Mockito.anyInt())).thenReturn("");
        Mockito.when(gl.glGetProgramInfoLog(Mockito.anyInt())).thenReturn("");
        Mockito.doNothing().when(gl).glShaderSource(Mockito.anyInt(), Mockito.any());
        Mockito.doNothing().when(gl).glCompileShader(Mockito.anyInt());
        Mockito.doNothing().when(gl).glAttachShader(Mockito.anyInt(), Mockito.anyInt());
        Mockito.doNothing().when(gl).glLinkProgram(Mockito.anyInt());
        Mockito.doNothing().when(gl).glDeleteShader(Mockito.anyInt());
        Mockito.doNothing().when(gl).glDeleteProgram(Mockito.anyInt());

        MapState state = new MapState();
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());

        MapRenderSystem renderSystem = new MapRenderSystem();
        MapRenderDataSystem dataSystem = new MapRenderDataSystem();

        World world = new World(new WorldConfigurationBuilder()
                .with(
                        renderSystem,
                        new MapInitSystem(new ProvidedMapStateProvider(state)),
                        dataSystem,
                        new PlayerCameraSystem()
                )
                .build());

        SpriteBatchMapRenderer renderer = Mockito.mock(SpriteBatchMapRenderer.class);
        renderSystem.setMapRenderer(renderer);
        renderSystem.setCameraProvider(world.getSystem(PlayerCameraSystem.class));

        world.process();

        var map = net.lapidist.colony.map.MapUtils.findMap(world).orElseThrow();
        var tile = map.getTiles().first();
        var tc = world.getMapper(net.lapidist.colony.components.maps.TileComponent.class)
                .get(tile);
        tc.setDirty(true);
        dataSystem.addDirtyIndex(0);
        map.incrementVersion();

        world.process();
        world.process();

        Mockito.verify(renderer).invalidateTiles(Mockito.any(IntArray.class));
        world.dispose();
    }
}
