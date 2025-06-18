package net.lapidist.colony.tests.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.client.screens.LoadGameScreen;
import net.lapidist.colony.client.screens.MainMenuScreen;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.save.io.GameStateIO;
import net.lapidist.colony.components.state.MapState;
import org.mockito.MockedConstruction;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mockConstruction;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class LoadGameScreenTest {

    private static Table getRoot(final LoadGameScreen screen) throws Exception {
        Field f = screen.getClass().getSuperclass().getDeclaredField("root");
        f.setAccessible(true);
        return (Table) f.get(screen);
    }

    private static Table getList(final LoadGameScreen screen) throws Exception {
        Table root = getRoot(screen);
        ScrollPane scroll = (ScrollPane) root.getChildren().get(1);
        return (Table) scroll.getActor();
    }

    private static Stage getStage(final LoadGameScreen screen) throws Exception {
        Field f = screen.getClass().getSuperclass().getDeclaredField("stage");
        f.setAccessible(true);
        return (Stage) f.get(screen);
    }

    @Test
    public void backButtonReturnsToMainMenu() throws Exception {
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(new net.lapidist.colony.settings.Settings());
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            LoadGameScreen screen = new LoadGameScreen(colony);
            Table root = getRoot(screen);
            TextButton back = (TextButton) root.getChildren().peek();
            back.fire(new ChangeListener.ChangeEvent());
            verify(colony).setScreen(isA(MainMenuScreen.class));
            screen.dispose();
        }
    }

    @Test
    public void escapeReturnsToMainMenu() throws Exception {
        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(new net.lapidist.colony.settings.Settings());
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            LoadGameScreen screen = new LoadGameScreen(colony);
            Stage stage = getStage(screen);
            stage.keyDown(Input.Keys.ESCAPE);
            verify(colony).setScreen(isA(MainMenuScreen.class));
            screen.dispose();
        }
    }

    @Test
    public void loadButtonStartsGame() throws Exception {
        String save = "test-" + UUID.randomUUID();
        Path folder = Path.of(
                System.getProperty("user.home"),
                ".colony",
                "saves"
        );
        Files.createDirectories(folder);
        final int size = 60;
        MapState state = MapState.builder()
                .width(size)
                .height(size)
                .build();
        GameStateIO.save(state, folder.resolve(save + Paths.AUTOSAVE_SUFFIX));

        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(new net.lapidist.colony.settings.Settings());
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            LoadGameScreen screen = new LoadGameScreen(colony);
            Table list = getList(screen);
            Table row = null;
            for (com.badlogic.gdx.scenes.scene2d.Actor child : list.getChildren()) {
                if (child instanceof Table t) {
                    TextButton btn = (TextButton) t.getChildren().first();
                    if (save.equals(btn.getText().toString())) {
                        row = t;
                        break;
                    }
                }
            }
            TextButton load = (TextButton) row.getChildren().get(0);
            load.fire(new ChangeListener.ChangeEvent());

            verify(colony).startGame(save, size, size);
            Files.deleteIfExists(folder.resolve(save + Paths.AUTOSAVE_SUFFIX));
            screen.dispose();
        }
    }

    @Test
    public void filteringHidesNonMatchingEntries() throws Exception {
        String prefix = "filter-test-";
        String match = prefix + "match-" + UUID.randomUUID();
        String other = prefix + "other-" + UUID.randomUUID();
        Path folder = Path.of(
                System.getProperty("user.home"),
                ".colony",
                "saves"
        );
        Files.createDirectories(folder);
        for (String existing : Paths.get().listAutosaves()) {
            if (existing.startsWith(prefix)) {
                Files.deleteIfExists(Paths.get().getAutosave(existing));
            }
        }
        MapState state = MapState.builder()
                .width(MapState.DEFAULT_WIDTH)
                .height(MapState.DEFAULT_HEIGHT)
                .build();
        GameStateIO.save(state, folder.resolve(match + Paths.AUTOSAVE_SUFFIX));
        GameStateIO.save(state, folder.resolve(other + Paths.AUTOSAVE_SUFFIX));

        Colony colony = mock(Colony.class);
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            LoadGameScreen screen = new LoadGameScreen(colony);
            Table root = getRoot(screen);
            TextField filter = (TextField) root.getChildren().first();
            filter.setText(match);
            filter.fire(new ChangeListener.ChangeEvent());
            Table list = getList(screen);
            boolean hasMatch = false;
            boolean hasOther = false;
            for (com.badlogic.gdx.scenes.scene2d.Actor child : list.getChildren()) {
                if (child instanceof Table t) {
                    TextButton btn = (TextButton) t.getChildren().first();
                    String name = btn.getText().toString();
                    if (name.equals(match)) {
                        hasMatch = true;
                    }
                    if (name.equals(other)) {
                        hasOther = true;
                    }
                }
            }
            assertTrue(hasMatch);
            assertFalse(hasOther);
            screen.dispose();
        }

        Files.deleteIfExists(folder.resolve(match + Paths.AUTOSAVE_SUFFIX));
        Files.deleteIfExists(folder.resolve(other + Paths.AUTOSAVE_SUFFIX));
    }
}
