package net.lapidist.colony.tests.client.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.resources.ResourceData;
import net.lapidist.colony.components.state.resources.ResourceGatherRequestData;
import net.lapidist.colony.components.state.map.TileData;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/** Tests resource gathering with {@link SelectionSystem}. */
@RunWith(GdxTestRunner.class)
public class SelectionSystemResourceTypeTest {

    @Test
    public void gatherKeyUsesSelectedStone() {
        MapState state = new MapState();
        ResourceData res = new ResourceData(1, 1, 0);
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .selected(true)
                .resources(res)
                .build());

        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        SelectionSystem system = new SelectionSystem(client, keys);
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(),
                        new CameraInputSystem(keys), new MapRenderDataSystem(), system)
                .build());

        Input input = mock(Input.class);
        Gdx.input = input;
        when(input.isKeyJustPressed(keys.getKey(KeyAction.SELECT_WOOD))).thenReturn(false, false);
        when(input.isKeyJustPressed(keys.getKey(KeyAction.SELECT_STONE))).thenReturn(true, false);
        when(input.isKeyJustPressed(keys.getKey(KeyAction.SELECT_FOOD))).thenReturn(false, false);
        when(input.isKeyJustPressed(keys.getKey(KeyAction.GATHER))).thenReturn(false, true);

        world.process();
        world.process();

        ArgumentCaptor<ResourceGatherRequestData> captor =
                ArgumentCaptor.forClass(ResourceGatherRequestData.class);
        verify(client).sendGatherRequest(captor.capture());
        assertEquals(Registries.resources().get("STONE").id(), captor.getValue().resourceId());
    }

    @Test
    public void tapUsesSelectedFood() {
        MapState state = new MapState();
        ResourceData res = new ResourceData(0, 0, 2);
        TileData tile = TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .resources(res)
                .build();
        state.putTile(tile);

        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        SelectionSystem system = new SelectionSystem(client, keys);
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(),
                        new CameraInputSystem(keys), new MapRenderDataSystem(), system)
                .build());
        world.process();

        Input input = mock(Input.class);
        Gdx.input = input;
        when(input.isKeyJustPressed(keys.getKey(KeyAction.SELECT_WOOD))).thenReturn(false);
        when(input.isKeyJustPressed(keys.getKey(KeyAction.SELECT_STONE))).thenReturn(false);
        when(input.isKeyJustPressed(keys.getKey(KeyAction.SELECT_FOOD))).thenReturn(true);

        system.process();
        when(input.isKeyJustPressed(anyInt())).thenReturn(false);

        PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
        ((com.badlogic.gdx.graphics.OrthographicCamera) camera.getCamera()).position.set(
                net.lapidist.colony.components.GameConstants.TILE_SIZE / 2f,
                net.lapidist.colony.components.GameConstants.TILE_SIZE / 2f,
                0
        );
        camera.getCamera().update();

        com.badlogic.gdx.math.Vector2 screenCoords =
                net.lapidist.colony.client.graphics.CameraUtils.worldToScreenCoords(
                        camera.getViewport(), 0, 0);
        system.setSelectMode(true);
        system.tap(screenCoords.x, screenCoords.y);

        ArgumentCaptor<ResourceGatherRequestData> captor =
                ArgumentCaptor.forClass(ResourceGatherRequestData.class);
        verify(client).sendGatherRequest(captor.capture());
        assertEquals(Registries.resources().get("FOOD").id(), captor.getValue().resourceId());
    }
}
