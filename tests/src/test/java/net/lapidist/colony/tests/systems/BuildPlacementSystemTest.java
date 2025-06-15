package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.KeyBindings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class BuildPlacementSystemTest {

    @Test
    public void buildKeyTogglesMode() {
        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        BuildPlacementSystem system = new BuildPlacementSystem(client, keys);
        World world = new World(new WorldConfigurationBuilder()
                .with(new PlayerCameraSystem(), new CameraInputSystem(keys), system)
                .build());

        Input input = mock(Input.class);
        Gdx.input = input;
        when(input.isKeyJustPressed(keys.getKey(KeyAction.BUILD))).thenReturn(true);

        world.process();

        assertTrue(system.isBuildMode());
    }

    @Test
    public void removeKeyTogglesMode() {
        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        BuildPlacementSystem system = new BuildPlacementSystem(client, keys);
        World world = new World(new WorldConfigurationBuilder()
                .with(new PlayerCameraSystem(), new CameraInputSystem(keys), system)
                .build());

        Input input = mock(Input.class);
        Gdx.input = input;
        when(input.isKeyJustPressed(keys.getKey(KeyAction.REMOVE))).thenReturn(true);

        world.process();

        assertTrue(system.isRemoveMode());
    }

    @Test
    public void tapPlacesBuildingWhenEnabled() {
        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build();
        state.putTile(tile);

        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        BuildPlacementSystem system = new BuildPlacementSystem(client, keys);
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(), new CameraInputSystem(keys), system)
                .build());
        world.process();

        system.setBuildMode(true);

        PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
        ((com.badlogic.gdx.graphics.OrthographicCamera) camera.getCamera()).position.set(
                GameConstants.TILE_SIZE / 2f,
                GameConstants.TILE_SIZE / 2f,
                0f
        );
        camera.getCamera().update();

        Vector2 screen = CameraUtils.worldToScreenCoords(camera.getViewport(), 0, 0);
        system.tap(screen.x, screen.y);

        verify(client).sendBuildRequest(any());
    }

    @Test
    public void tapRemovesBuildingWhenEnabled() {
        MapState state = new MapState();
        state.buildings().add(new net.lapidist.colony.components.state.BuildingData(0, 0, "house"));
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());

        GameClient client = mock(GameClient.class);
        KeyBindings keys = new KeyBindings();
        BuildPlacementSystem system = new BuildPlacementSystem(client, keys);
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new PlayerCameraSystem(), new CameraInputSystem(keys), system)
                .build());
        world.process();

        system.setRemoveMode(true);

        PlayerCameraSystem camera = world.getSystem(PlayerCameraSystem.class);
        ((com.badlogic.gdx.graphics.OrthographicCamera) camera.getCamera()).position.set(
                GameConstants.TILE_SIZE / 2f,
                GameConstants.TILE_SIZE / 2f,
                0f
        );
        camera.getCamera().update();

        Vector2 screen = CameraUtils.worldToScreenCoords(camera.getViewport(), 0, 0);
        system.tap(screen.x, screen.y);

        verify(client).sendRemoveBuildingRequest(any());
    }
}
