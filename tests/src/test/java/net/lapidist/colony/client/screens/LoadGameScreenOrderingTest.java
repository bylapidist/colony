package net.lapidist.colony.client.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

@RunWith(GdxTestRunner.class)
public class LoadGameScreenOrderingTest {

    private static final long OLDER_DELTA = 10_000L;

    @SuppressWarnings("unchecked")
    @Test
    public void newerSavesAppearFirst() throws Exception {
        String newer = "newer-" + UUID.randomUUID();
        String older = "older-" + UUID.randomUUID();
        Paths.get().createGameFoldersIfNotExists();
        Path newerFile = Paths.get().getAutosave(newer);
        Path olderFile = Paths.get().getAutosave(older);
        Files.writeString(newerFile, "n");
        Files.writeString(olderFile, "o");
        long now = System.currentTimeMillis();
        Files.setLastModifiedTime(newerFile, FileTime.fromMillis(now));
        Files.setLastModifiedTime(olderFile, FileTime.fromMillis(now - OLDER_DELTA));

        Colony colony = mock(Colony.class);
        when(colony.getSettings()).thenReturn(new net.lapidist.colony.settings.Settings());
        try (MockedConstruction<SpriteBatch> ignored = mockConstruction(SpriteBatch.class)) {
            LoadGameScreen screen = new LoadGameScreen(colony);
            Method m = LoadGameScreen.class.getDeclaredMethod("listSaves");
            m.setAccessible(true);
            List<String> result = (List<String>) m.invoke(screen);
            assertEquals(newer, result.get(0));
            assertEquals(older, result.get(1));
            screen.dispose();
        }

        Files.deleteIfExists(newerFile);
        Files.deleteIfExists(olderFile);
    }
}
