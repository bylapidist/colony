package net.lapidist.colony.tests.core.io;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import net.lapidist.colony.io.PathService;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.io.TestPathService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AbstractPathServiceTest {

    private Files oldFiles;

    @Before
    public void setUp() {
        oldFiles = Gdx.files;
        Gdx.files = null;
    }

    @After
    public void tearDown() {
        Gdx.files = oldFiles;
    }

    @Test
    public void createsSettingsFileAndFolders() throws Exception {
        Path temp = java.nio.file.Files.createTempDirectory("aps-test");
        Path gameFolder = temp.resolve(".colony");
        PathService service = new TestPathService(gameFolder);
        Paths paths = new Paths(service);

        Path settings = paths.getSettingsFile();

        assertTrue(java.nio.file.Files.exists(gameFolder));
        assertTrue(java.nio.file.Files.exists(gameFolder.resolve("saves")));
        assertTrue(java.nio.file.Files.exists(gameFolder.resolve("mods")));
        assertEquals(gameFolder.resolve("settings.conf"), settings);
    }

    @Test
    public void listAutosavesHandlesMissingFolder() throws Exception {
        Path temp = java.nio.file.Files.createTempDirectory("aps-list-missing");
        Path gameFolder = temp.resolve(".colony");
        PathService service = new TestPathService(gameFolder);
        Paths paths = new Paths(service);

        List<String> autosaves = paths.listAutosaves();

        assertTrue(autosaves.isEmpty());
    }

    @Test
    public void listAutosavesReturnsAutosaveNames() throws Exception {
        Path temp = java.nio.file.Files.createTempDirectory("aps-list");
        Path gameFolder = temp.resolve(".colony");
        PathService service = new TestPathService(gameFolder);
        Paths paths = new Paths(service);

        java.nio.file.Files.createDirectories(gameFolder.resolve("saves"));
        java.nio.file.Files.writeString(paths.getAutosave("one"), "a");
        java.nio.file.Files.writeString(paths.getAutosave("two"), "b");

        List<String> autosaves = paths.listAutosaves();
        java.util.Collections.sort(autosaves);

        assertEquals(Arrays.asList("one", "two"), autosaves);
    }
}
