package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.SelectionSystem;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.components.state.ResourceGatherRequestData;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class InputSystemResourceTypeTest {

    @Test
    public void gatherKeyUsesTileResourceType() {
        MapState state = new MapState();
        ResourceData res = new ResourceData(0, 2, 1);
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .selected(true)
                .resources(res)
                .build());

        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        CameraInputSystem cameraInput = new CameraInputSystem(keys);
        SelectionSystem selectionSystem = new SelectionSystem(client, keys);
        BuildPlacementSystem buildSystem = new BuildPlacementSystem(client, keys);

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(),
                        cameraInput, selectionSystem, buildSystem)
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
    public void tapSendsTileResourceType() {
        MapState state = new MapState();
        ResourceData res = new ResourceData(1, 1, 0);
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .passable(true)
                .resources(res)
                .build();
        state.putTile(tile);

        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        CameraInputSystem cameraInput = new CameraInputSystem(keys);
        SelectionSystem selectionSystem = new SelectionSystem(client, keys);
        BuildPlacementSystem buildSystem = new BuildPlacementSystem(client, keys);
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(),
                        cameraInput, selectionSystem, buildSystem)
                .build());
        world.process();

        PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
        ((com.badlogic.gdx.graphics.OrthographicCamera) camera.getCamera()).position.set(
                GameConstants.TILE_SIZE / 2f,
                GameConstants.TILE_SIZE / 2f,
                0
        );
        camera.getCamera().update();

        Vector2 screenCoords = CameraUtils.worldToScreenCoords(camera.getViewport(), 0, 0);
        selectionSystem.tap(screenCoords.x, screenCoords.y);

        ArgumentCaptor<ResourceGatherRequestData> captor =
                ArgumentCaptor.forClass(ResourceGatherRequestData.class);
        verify(client).sendGatherRequest(captor.capture());
        assertEquals(Registries.resources().get("WOOD").id(), captor.getValue().resourceId());
    }
}
