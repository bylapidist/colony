package net.lapidist.colony.client.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
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

        MapUiBuilder.build(stage, world, client, colony);

        TextButton buildButton = stage.getRoot().findActor("buildButton");
        TextButton removeButton = stage.getRoot().findActor("removeButton");

        // enable build mode
        buildButton.toggle();
        assertTrue(buildSystem.isBuildMode());
        assertTrue(buildButton.isChecked());
        assertFalse(buildSystem.isRemoveMode());
        assertFalse(removeButton.isChecked());

        // enable remove mode, should disable build
        removeButton.toggle();
        assertTrue(buildSystem.isRemoveMode());
        assertTrue(removeButton.isChecked());
        assertFalse(buildSystem.isBuildMode());
        assertFalse(buildButton.isChecked());

        // disable remove mode
        removeButton.toggle();
        assertFalse(buildSystem.isRemoveMode());
        assertFalse(removeButton.isChecked());
    }
}
