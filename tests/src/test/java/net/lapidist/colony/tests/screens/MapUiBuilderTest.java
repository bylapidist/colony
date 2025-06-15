package net.lapidist.colony.tests.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.screens.MapUi;
import net.lapidist.colony.client.screens.MapUiBuilder;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.client.systems.BuildPlacementSystem;
import net.lapidist.colony.client.systems.CameraInputSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.tests.GdxTestRunner;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.Input;
import net.lapidist.colony.util.I18n;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class MapUiBuilderTest {

    @Test
    public void enterShowsChatInput() {
        Stage stage = new Stage(new ScreenViewport(), mock(Batch.class));
        World world = new World(new WorldConfigurationBuilder().build());
        GameClient client = mock(GameClient.class);
        Colony colony = mock(Colony.class);
        Settings settings = new Settings();
        when(colony.getSettings()).thenReturn(settings);
        MapUi ui = MapUiBuilder.build(stage, world, client, colony);

        stage.keyDown(settings.getKeyBindings().getKey(KeyAction.CHAT));

        assertTrue(ui.getChatBox().isInputVisible());
    }

    @Test
    public void togglesMinimapVisibility() {
        Stage stage = new Stage(new ScreenViewport(), mock(Batch.class));
        World world = new World(new WorldConfigurationBuilder().build());
        GameClient client = mock(GameClient.class);
        Colony colony = mock(Colony.class);
        Settings settings = new Settings();
        when(colony.getSettings()).thenReturn(settings);

        MapUi ui = MapUiBuilder.build(stage, world, client, colony);

        TextButton minimapButton = stage.getRoot().findActor("minimapButton");
        String expectedButton = I18n.get("map.minimap") + " ["
                + Input.Keys.toString(settings.getKeyBindings().getKey(KeyAction.MINIMAP)) + "]";
        assertEquals(expectedButton, minimapButton.getText().toString());

        boolean initial = ui.getMinimapActor().isVisible();
        boolean initialChecked = minimapButton.isChecked();
        assertEquals(initial, initialChecked);

        stage.keyDown(settings.getKeyBindings().getKey(KeyAction.MINIMAP));

        assertEquals(!initial, ui.getMinimapActor().isVisible());
        assertEquals(!initialChecked, minimapButton.isChecked());

        stage.keyDown(settings.getKeyBindings().getKey(KeyAction.MINIMAP));

        assertEquals(initial, ui.getMinimapActor().isVisible());
        assertEquals(initialChecked, minimapButton.isChecked());
    }

    @Test
    public void buildButtonEnablesBuildMode() {
        Stage stage = new Stage(new ScreenViewport(), mock(Batch.class));
        BuildPlacementSystem buildSystem = new BuildPlacementSystem(
                mock(GameClient.class),
                new Settings().getKeyBindings()
        );
        World world = new World(new WorldConfigurationBuilder()
                .with(
                        new PlayerCameraSystem(),
                        new CameraInputSystem(new Settings().getKeyBindings()),
                        buildSystem
                )
                .build());
        GameClient client = mock(GameClient.class);
        Colony colony = mock(Colony.class);
        Settings settings = new Settings();
        when(colony.getSettings()).thenReturn(settings);

        MapUi ui = MapUiBuilder.build(stage, world, client, colony);

        TextButton buildButton = stage.getRoot().findActor("buildButton");
        String expected = I18n.get("map.build") + " ["
                + Input.Keys.toString(settings.getKeyBindings().getKey(KeyAction.BUILD)) + "]";
        assertEquals(expected, buildButton.getText().toString());
        buildButton.toggle();
        assertTrue(buildSystem.isBuildMode());
    }
}
