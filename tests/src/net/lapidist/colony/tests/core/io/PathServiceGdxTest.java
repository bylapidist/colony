package net.lapidist.colony.tests.core.io;

import com.badlogic.gdx.Gdx;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/**
 * Verifies that {@link Paths} resolves locations using LibGDX when
 * a Files implementation is present.
 */
@RunWith(GdxTestRunner.class)
public class PathServiceGdxTest {

    @Test
    public void resolvesSavePathUsingGdxFiles() throws Exception {
        Path expected = Gdx.files.external(".colony/saves/foo.dat").file().toPath();
        Path actual = Paths.get().getSave("foo");
        assertEquals(expected.toAbsolutePath().normalize(), actual.toAbsolutePath().normalize());
    }
}
