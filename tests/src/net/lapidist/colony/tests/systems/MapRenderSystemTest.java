package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.renderers.MapRenderer;
import net.lapidist.colony.client.systems.MapRenderSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import net.lapidist.colony.tests.GdxTestRunner;
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
        state.tiles().put(new TilePos(0, 0), tile);

        MapRenderSystem renderSystem = new MapRenderSystem();

        World world = new World(new WorldConfigurationBuilder()
                .with(
                        renderSystem,
                        new MapInitSystem(new ProvidedMapStateProvider(state)),
                        new PlayerCameraSystem()
                )
                .build());

        renderSystem.setMapRenderer(Mockito.mock(MapRenderer.class));

        world.setDelta(0f);
        world.process();

        MapRenderSystem system = world.getSystem(MapRenderSystem.class);
        Field mapField = MapRenderSystem.class.getDeclaredField("map");
        mapField.setAccessible(true);
        Object map = mapField.get(system);

        assertNotNull("MapRenderSystem should find map after initialization", map);
        world.dispose();
    }
}
