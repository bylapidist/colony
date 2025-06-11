package net.lapidist.colony.tests.core.io;

import com.badlogic.gdx.files.FileHandle;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class FileLocationTest {

    @Test
    public void internalResolvesExistingFile() {
        FileHandle handle = FileLocation.INTERNAL.getFile("skin/default.json");
        assertTrue(handle.exists());
    }

    @Test
    public void absoluteResolverResolvesFile() throws IOException {
        Path tmp = Files.createTempFile("filelocation", ".tmp");
        FileHandle handle = FileLocation.ABSOLUTE.getResolver().resolve(tmp.toString());
        assertEquals(tmp.toAbsolutePath().toString(), handle.file().getAbsolutePath());
        Files.deleteIfExists(tmp);
    }
}
