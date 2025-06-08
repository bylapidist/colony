package net.lapidist.colony.tests.core.io;

import net.lapidist.colony.io.Paths;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AutosaveDeleteTest {

    @Test
    public void deleteAutosaveRemovesFile() throws Exception {
        Paths.createGameFoldersIfNotExists();
        Path file = Paths.getAutosave("delete-test");
        Files.writeString(file, "dummy");
        assertTrue(Files.exists(file));
        Paths.deleteAutosave("delete-test");
        assertFalse(Files.exists(file));
    }
}
