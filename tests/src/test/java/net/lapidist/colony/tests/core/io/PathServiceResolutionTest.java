package net.lapidist.colony.tests.core.io;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import net.lapidist.colony.io.PathService;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.io.UnixPathService;
import net.lapidist.colony.io.WindowsPathService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PathServiceResolutionTest {

    private String oldUserHome;
    private String oldOsName;
    private Files oldFiles;

    @Before
    public void setUp() {
        oldUserHome = System.getProperty("user.home");
        oldOsName = System.getProperty("os.name");
        oldFiles = Gdx.files;
        Gdx.files = null;
    }

    @After
    public void tearDown() throws Exception {
        System.setProperty("user.home", oldUserHome);
        System.setProperty("os.name", oldOsName);
        Gdx.files = oldFiles;
    }

    @Test
    public void createDefaultServiceSelectsWindows() {
        System.setProperty("os.name", "Windows 10");
        assertTrue(Paths.createDefaultService() instanceof WindowsPathService);
    }

    @Test
    public void createDefaultServiceSelectsUnix() {
        System.setProperty("os.name", "Linux");
        assertTrue(Paths.createDefaultService() instanceof UnixPathService);
    }

    @Test
    public void resolvesSavePathUnix() throws Exception {
        Path tmp = java.nio.file.Files.createTempDirectory("unix");
        System.setProperty("user.home", tmp.toString());
        PathService service = new UnixPathService();
        Paths paths = new Paths(service);

        Path save = paths.getSave("foo");
        Path expected = java.nio.file.Paths.get(tmp.toString(), ".colony", "saves", "foo.dat");
        assertEquals(expected, save);
    }

    @Test
    public void resolvesSavePathWindows() throws Exception {
        Path tmp = java.nio.file.Files.createTempDirectory("win");
        System.setProperty("user.home", tmp.toString());
        PathService service = new WindowsPathService();
        Paths paths = new Paths(service);

        Path save = paths.getSave("bar");
        Path expected = java.nio.file.Paths.get(tmp.toString(), ".colony", "saves", "bar.dat");
        assertEquals(expected, save);
    }
}
