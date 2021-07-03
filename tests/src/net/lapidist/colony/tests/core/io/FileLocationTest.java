package net.lapidist.colony.tests.core.io;

import com.badlogic.gdx.files.FileHandle;
import net.lapidist.colony.core.io.FileLocation;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(GdxTestRunner.class)
public class FileLocationTest {

    @Ignore
    public void testReadFileClassPath() {
    }

    @Test
    public final void testReadFileInternal() {
        FileHandle file = FileLocation.INTERNAL.getFile("textures/grass.png");
        assertNotNull(file);
    }

    @Ignore
    public void testReadFileLocal() {
    }

    @Ignore
    public void testReadFileExternal() {
    }

    @Ignore
    public void testReadFileAbsolute() {
    }
}
