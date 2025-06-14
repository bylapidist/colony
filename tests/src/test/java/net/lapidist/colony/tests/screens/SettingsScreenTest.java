package net.lapidist.colony.tests.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.screens.KeybindsScreen;
import net.lapidist.colony.client.screens.MainMenuScreen;
import net.lapidist.colony.client.screens.SettingsScreen;
import net.lapidist.colony.client.screens.GraphicsSettingsScreen;
import net.lapidist.colony.settings.Settings;
import org.mockito.MockedConstruction;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mockConstruction;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class SettingsScreenTest {

    private static final int KEYBINDS_INDEX = 4;
    private static final int GRAPHICS_INDEX = 5;
    private static final int BACK_BUTTON_INDEX = 6;

    private static Table getRoot(final SettingsScreen screen) throws Exception {
        Field f = screen.getClass().getSuperclass().getDeclaredField("root");
        f.setAccessible(true);
        return (Table) f.get(screen);
    }

    private static Stage getStage(final SettingsScreen screen) throws Exception {
        Field f = screen.getClass().getSuperclass().getDeclaredField("stage");
        f.setAccessible(true);
        return (Stage) f.get(screen);
    }

    @Test
    public void keybindsButtonOpensKeybindsScreen() throws Exception {
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(new Settings());
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            SettingsScreen screen = new SettingsScreen(colony);
            TextButton keybinds = (TextButton) getRoot(screen).getChildren().get(KEYBINDS_INDEX);
            keybinds.fire(new ChangeListener.ChangeEvent());
            verify(colony).setScreen(isA(KeybindsScreen.class));
            screen.dispose();
        }
    }

    @Test
    public void graphicsButtonOpensGraphicsScreen() throws Exception {
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(new Settings());
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            SettingsScreen screen = new SettingsScreen(colony);
            TextButton graphics = (TextButton) getRoot(screen).getChildren().get(GRAPHICS_INDEX);
            graphics.fire(new ChangeListener.ChangeEvent());
            verify(colony).setScreen(isA(GraphicsSettingsScreen.class));
            screen.dispose();
        }
    }

    @Test
    public void backButtonReturnsToMainMenu() throws Exception {
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(new Settings());
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            SettingsScreen screen = new SettingsScreen(colony);
            TextButton back = (TextButton) getRoot(screen).getChildren().get(BACK_BUTTON_INDEX);
            back.fire(new ChangeListener.ChangeEvent());
            verify(colony).setScreen(isA(MainMenuScreen.class));
            screen.dispose();
        }
    }

    @Test
    public void escapeReturnsToMainMenu() throws Exception {
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(new Settings());
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            SettingsScreen screen = new SettingsScreen(colony);
            Stage stage = getStage(screen);
            stage.keyDown(Input.Keys.ESCAPE);
            verify(colony).setScreen(isA(MainMenuScreen.class));
            screen.dispose();
        }
    }
}
