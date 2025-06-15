package net.lapidist.colony.tests.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.screens.LoadGameScreen;
import net.lapidist.colony.client.screens.MainMenuScreen;
import net.lapidist.colony.client.screens.ModSelectionScreen;
import net.lapidist.colony.client.screens.SettingsScreen;
import org.mockito.MockedConstruction;
import static org.mockito.Mockito.mockConstruction;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class MainMenuScreenTest {

    private static final int NEW_GAME_INDEX = 2;
    private static final int LOAD_GAME_INDEX = 3;
    private static final int SETTINGS_INDEX = 4;

    private static Table getRoot(final MainMenuScreen screen) throws Exception {
        Field f = screen.getClass().getSuperclass().getDeclaredField("root");
        f.setAccessible(true);
        return (Table) f.get(screen);
    }

    @Test
    public void clickingNewGameOpensNewGameScreen() throws Exception {
        Colony colony = mock(Colony.class);
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            MainMenuScreen screen = new MainMenuScreen(colony);
            TextButton newGame = (TextButton) getRoot(screen).getChildren().get(NEW_GAME_INDEX);
            newGame.fire(new ChangeListener.ChangeEvent());
            verify(colony).setScreen(isA(ModSelectionScreen.class));
            screen.dispose();
        }
    }

    @Test
    public void clickingLoadGameOpensLoadGameScreen() throws Exception {
        Colony colony = mock(Colony.class);
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            MainMenuScreen screen = new MainMenuScreen(colony);
            TextButton load = (TextButton) getRoot(screen).getChildren().get(LOAD_GAME_INDEX);
            load.fire(new ChangeListener.ChangeEvent());
            verify(colony).setScreen(isA(LoadGameScreen.class));
            screen.dispose();
        }
    }

    @Test
    public void clickingSettingsOpensSettingsScreen() throws Exception {
        Colony colony = mock(Colony.class);
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            MainMenuScreen screen = new MainMenuScreen(colony);
            TextButton settings = (TextButton) getRoot(screen).getChildren().get(SETTINGS_INDEX);
            settings.fire(new ChangeListener.ChangeEvent());
            verify(colony).setScreen(isA(SettingsScreen.class));
            screen.dispose();
        }
    }
}
