package net.lapidist.colony.client.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/** Tests for build and remove button state changes. */
@RunWith(GdxTestRunner.class)
public class MapUiButtonStateTest {

    @Test
    public void togglingButtonsUpdatesModesAndStates() {
        Stage stage = new Stage(new ScreenViewport(), mock(Batch.class));
        Settings settings = new Settings();
        BuildPlacementSystem buildSystem = new BuildPlacementSystem(
                mock(GameClient.class),
                settings.getKeyBindings()
        );
        World world = new World(new WorldConfigurationBuilder()
                .with(
                        new PlayerCameraSystem(),
                        new CameraInputSystem(settings.getKeyBindings()),
                        buildSystem
                )
                .build());
        GameClient client = mock(GameClient.class);
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(settings);

        MapUiBuilder.build(stage, world, client, colony, new MapScreenEventHandler());

        TextButton buildButton = stage.getRoot().findActor("buildButton");
        TextButton removeButton = stage.getRoot().findActor("removeButton");
        net.lapidist.colony.client.ui.BuildMenuActor buildMenu = stage.getRoot().findActor("buildMenu");
        assertFalse(buildMenu.isVisible());

        // enable build mode
        buildButton.toggle();
        stage.act(0f);
        assertTrue(buildSystem.isBuildMode());
        assertTrue(buildButton.isChecked());
        assertFalse(buildSystem.isRemoveMode());
        assertFalse(removeButton.isChecked());
        assertTrue(buildMenu.isVisible());

        // enable remove mode, should disable build
        removeButton.toggle();
        stage.act(0f);
        assertTrue(buildSystem.isRemoveMode());
        assertTrue(removeButton.isChecked());
        assertFalse(buildSystem.isBuildMode());
        assertFalse(buildButton.isChecked());
        assertFalse(buildMenu.isVisible());

        // disable remove mode
        removeButton.toggle();
        assertFalse(buildSystem.isRemoveMode());
        assertFalse(removeButton.isChecked());
    }

    @Test
    public void mapAndMinimapButtonsToggleStates() {
        Stage stage = new Stage(new ScreenViewport(), mock(Batch.class));
        Settings settings = new Settings();
        BuildPlacementSystem buildSystem = new BuildPlacementSystem(
                mock(GameClient.class),
                settings.getKeyBindings()
        );
        World world = new World(new WorldConfigurationBuilder()
                .with(
                        new PlayerCameraSystem(),
                        new CameraInputSystem(settings.getKeyBindings()),
                        buildSystem
                )
                .build());
        GameClient client = mock(GameClient.class);
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(settings);

        MapUi ui = MapUiBuilder.build(stage, world, client, colony, new MapScreenEventHandler());

        PlayerCameraSystem cameraSystem = world.getSystem(PlayerCameraSystem.class);
        TextButton mapButton = stage.getRoot().findActor("mapButton");
        TextButton minimapButton = stage.getRoot().findActor("minimapButton");

        assertEquals(PlayerCameraSystem.Mode.PLAYER, cameraSystem.getMode());
        assertFalse(mapButton.isChecked());

        // toggle map view
        mapButton.toggle();
        assertEquals(PlayerCameraSystem.Mode.MAP_OVERVIEW, cameraSystem.getMode());
        assertTrue(mapButton.isChecked());
        mapButton.toggle();
        assertEquals(PlayerCameraSystem.Mode.PLAYER, cameraSystem.getMode());
        assertFalse(mapButton.isChecked());

        // toggle minimap visibility
        boolean initial = ui.getMinimapActor().isVisible();
        boolean initialChecked = minimapButton.isChecked();
        minimapButton.toggle();
        assertEquals(!initial, ui.getMinimapActor().isVisible());
        assertEquals(!initialChecked, minimapButton.isChecked());
        minimapButton.toggle();
        assertEquals(initial, ui.getMinimapActor().isVisible());
        assertEquals(initialChecked, minimapButton.isChecked());
    }

    @Test
    public void keyPressesUpdateButtonStates() {
        Stage stage = new Stage(new ScreenViewport(), mock(Batch.class));
        Settings settings = new Settings();
        BuildPlacementSystem buildSystem = new BuildPlacementSystem(
                mock(GameClient.class),
                settings.getKeyBindings()
        );
        World world = new World(new WorldConfigurationBuilder()
                .with(
                        new PlayerCameraSystem(),
                        new CameraInputSystem(settings.getKeyBindings()),
                        buildSystem
                )
                .build());
        GameClient client = mock(GameClient.class);
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(settings);

        MapUiBuilder.build(stage, world, client, colony, new MapScreenEventHandler());

        TextButton buildButton = stage.getRoot().findActor("buildButton");
        TextButton removeButton = stage.getRoot().findActor("removeButton");

        Input input = mock(Input.class);
        Gdx.input = input;

        when(input.isKeyJustPressed(settings.getKeyBindings().getKey(KeyAction.BUILD))).thenReturn(true);
        world.process();
        stage.act(0f);

        assertTrue(buildButton.isChecked());
        assertFalse(removeButton.isChecked());

        when(input.isKeyJustPressed(settings.getKeyBindings().getKey(KeyAction.BUILD))).thenReturn(false);
        when(input.isKeyJustPressed(settings.getKeyBindings().getKey(KeyAction.REMOVE))).thenReturn(true);
        world.process();
        stage.act(0f);

        assertTrue(removeButton.isChecked());
        assertFalse(buildButton.isChecked());
    }

    @Test
    public void cameraHotkeyUpdatesMapButton() {
        Stage stage = new Stage(new ScreenViewport(), mock(Batch.class));
        Settings settings = new Settings();
        BuildPlacementSystem buildSystem = new BuildPlacementSystem(
                mock(GameClient.class),
                settings.getKeyBindings()
        );
        World world = new World(new WorldConfigurationBuilder()
                .with(
                        new PlayerCameraSystem(),
                        new CameraInputSystem(settings.getKeyBindings()),
                        buildSystem
                )
                .build());
        GameClient client = mock(GameClient.class);
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(settings);

        MapUiBuilder.build(stage, world, client, colony, new MapScreenEventHandler());

        TextButton mapButton = stage.getRoot().findActor("mapButton");
        PlayerCameraSystem cameraSystem = world.getSystem(PlayerCameraSystem.class);

        Input input = mock(Input.class);
        Gdx.input = input;

        when(input.isKeyJustPressed(settings.getKeyBindings().getKey(KeyAction.TOGGLE_CAMERA))).thenReturn(true);
        world.process();
        stage.act(0f);

        assertEquals(PlayerCameraSystem.Mode.MAP_OVERVIEW, cameraSystem.getMode());
        assertTrue(mapButton.isChecked());

        reset(input);
        when(input.isKeyJustPressed(settings.getKeyBindings().getKey(KeyAction.TOGGLE_CAMERA))).thenReturn(true);
        world.process();
        stage.act(0f);

        assertEquals(PlayerCameraSystem.Mode.PLAYER, cameraSystem.getMode());
        assertFalse(mapButton.isChecked());
    }
}
