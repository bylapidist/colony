package net.lapidist.colony.tests.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.screens.GraphicsSettingsScreen;
import net.lapidist.colony.client.screens.SettingsScreen;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import org.mockito.MockedConstruction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class GraphicsSettingsScreenTest {

    private static final int SCROLL_INDEX = 0;
    private static final int BUTTON_TABLE_INDEX = 1;
    private static final int AA_INDEX = 0;
    private static final int DAY_NIGHT_INDEX = 7;
    private static final int BACK_INDEX = 0;
    private static final int SAVE_INDEX = 1;

    private static Table getRoot(final GraphicsSettingsScreen screen) throws Exception {
        Field f = screen.getClass().getSuperclass().getDeclaredField("root");
        f.setAccessible(true);
        return (Table) f.get(screen);
    }

    private static Table getOptions(final GraphicsSettingsScreen screen) throws Exception {
        Table root = getRoot(screen);
        ScrollPane scroll = (ScrollPane) root.getChildren().get(SCROLL_INDEX);
        return (Table) scroll.getActor();
    }

    private static Table getButtons(final GraphicsSettingsScreen screen) throws Exception {
        Table root = getRoot(screen);
        return (Table) root.getChildren().get(BUTTON_TABLE_INDEX);
    }

    @Test
    public void saveAppliesCheckboxValues() throws Exception {
        Settings settings = new Settings();
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(settings);
        Stage stage = new Stage(new ScreenViewport(), mock(Batch.class));
        GraphicsSettingsScreen screen = new GraphicsSettingsScreen(colony, stage);
        Table options = getOptions(screen);
        CheckBox aa = (CheckBox) options.getChildren().get(AA_INDEX);
        aa.setChecked(true);
        CheckBox dn = (CheckBox) options.getChildren().get(DAY_NIGHT_INDEX);
        dn.setChecked(false);
        Table buttons = getButtons(screen);
        TextButton save = (TextButton) buttons.getChildren().get(SAVE_INDEX);
        save.fire(new ChangeListener.ChangeEvent());
        assertTrue(settings.getGraphicsSettings().isAntialiasingEnabled());
        assertFalse(settings.getGraphicsSettings().isDayNightCycleEnabled());
    }

    @Test
    public void backButtonReturnsToSettings() throws Exception {
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(new Settings());
        Stage stage = new Stage(new ScreenViewport(), mock(Batch.class));
        GraphicsSettingsScreen screen = new GraphicsSettingsScreen(colony, stage);
        Table buttons = getButtons(screen);
        TextButton back = (TextButton) buttons.getChildren().get(BACK_INDEX);
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            back.fire(new ChangeListener.ChangeEvent());
        }
        verify(colony).setScreen(isA(SettingsScreen.class));
    }
}
