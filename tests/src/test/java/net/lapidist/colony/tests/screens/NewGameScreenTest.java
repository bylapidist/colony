package net.lapidist.colony.tests.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.screens.MainMenuScreen;
import net.lapidist.colony.client.screens.NewGameScreen;
import net.lapidist.colony.settings.Settings;
import org.mockito.MockedConstruction;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mockConstruction;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class NewGameScreenTest {

    private static final int SCROLL_INDEX = 0;
    private static final int NAME_FIELD_INDEX = 1;
    private static final int SIZE_BUTTON_INDEX = 3;
    private static final int BUTTON_TABLE_INDEX = 1;
    private static final int BACK_BUTTON_INDEX = 0;
    private static final int START_BUTTON_INDEX = 1;
    private static final int MEDIUM_SIZE = 60;

    private static Table getRoot(final NewGameScreen screen) throws Exception {
        Field f = screen.getClass().getSuperclass().getDeclaredField("root");
        f.setAccessible(true);
        return (Table) f.get(screen);
    }

    private static Table getOptions(final NewGameScreen screen) throws Exception {
        Table root = getRoot(screen);
        ScrollPane scroll = (ScrollPane) root.getChildren().get(SCROLL_INDEX);
        return (Table) scroll.getActor();
    }

    private static Table getButtons(final NewGameScreen screen) throws Exception {
        Table root = getRoot(screen);
        return (Table) root.getChildren().get(BUTTON_TABLE_INDEX);
    }

    private static Stage getStage(final NewGameScreen screen) throws Exception {
        Field f = screen.getClass().getSuperclass().getDeclaredField("stage");
        f.setAccessible(true);
        return (Stage) f.get(screen);
    }

    @Test
    public void startButtonBeginsGameWithEnteredName() throws Exception {
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(new Settings());
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            NewGameScreen screen = new NewGameScreen(colony);
            Table options = getOptions(screen);
            TextField field = (TextField) options.getChildren().get(NAME_FIELD_INDEX);
            TextButton size = (TextButton) options.getChildren().get(SIZE_BUTTON_INDEX);
            Table buttons = getButtons(screen);
            TextButton start = (TextButton) buttons.getChildren().get(START_BUTTON_INDEX);
            field.setText("mysave");
            size.fire(new ChangeListener.ChangeEvent());
            start.fire(new ChangeListener.ChangeEvent());
            verify(colony).startGame("mysave", MEDIUM_SIZE, MEDIUM_SIZE);
            screen.dispose();
        }
    }

    @Test
    public void backButtonReturnsToMainMenu() throws Exception {
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(new Settings());
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            NewGameScreen screen = new NewGameScreen(colony);
            Table buttons = getButtons(screen);
            TextButton back = (TextButton) buttons.getChildren().get(BACK_BUTTON_INDEX);
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
            NewGameScreen screen = new NewGameScreen(colony);
            Stage stage = getStage(screen);
            stage.keyDown(Input.Keys.ESCAPE);
            verify(colony).setScreen(isA(MainMenuScreen.class));
            screen.dispose();
        }
    }
}
