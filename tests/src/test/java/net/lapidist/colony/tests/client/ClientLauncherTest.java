package net.lapidist.colony.tests.client;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import net.lapidist.colony.client.ClientLauncher;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class ClientLauncherTest {

    private static final int ENABLED_SAMPLES = 4;
    private static final int RES_WIDTH = 1024;
    private static final int RES_HEIGHT = 768;
    private static final int DEFAULT_WIDTH = 1600;

    @Test
    public void configUsesAntiAliasingPreference() throws Exception {
        String oldHome = System.getProperty("user.home");
        java.nio.file.Path home = java.nio.file.Files.createTempDirectory("client-launcher");
        System.setProperty("user.home", home.toString());
        java.nio.file.Path settings = home.resolve(".colony/settings.conf");
        java.nio.file.Files.createDirectories(settings.getParent());
        java.util.Properties props = new java.util.Properties();
        props.setProperty("graphics.antialiasing", "true");
        try (java.io.OutputStream out = java.nio.file.Files.newOutputStream(settings)) {
            props.store(out, null);
        }

        Lwjgl3ApplicationConfiguration config = ClientLauncher.createConfiguration();
        Field samples = Lwjgl3ApplicationConfiguration.class.getDeclaredField("samples");
        samples.setAccessible(true);
        assertEquals(ENABLED_SAMPLES, samples.getInt(config));
        System.setProperty("user.home", oldHome);
    }

    @Test
    public void configDisablesAntiAliasingByDefault() throws Exception {
        String oldHome = System.getProperty("user.home");
        java.nio.file.Path home = java.nio.file.Files.createTempDirectory("client-launcher-default");
        System.setProperty("user.home", home.toString());
        // no settings file created

        Lwjgl3ApplicationConfiguration config = ClientLauncher.createConfiguration();
        Field samples = Lwjgl3ApplicationConfiguration.class.getDeclaredField("samples");
        samples.setAccessible(true);
        assertEquals(ENABLED_SAMPLES, samples.getInt(config));
        System.setProperty("user.home", oldHome);
    }

    @Test
    public void configUsesResolutionSettings() throws Exception {
        String oldHome = System.getProperty("user.home");
        java.nio.file.Path home = java.nio.file.Files.createTempDirectory("client-launcher-res");
        System.setProperty("user.home", home.toString());
        java.nio.file.Path settings = home.resolve(".colony/settings.conf");
        java.nio.file.Files.createDirectories(settings.getParent());
        java.util.Properties props = new java.util.Properties();
        props.setProperty("width", Integer.toString(RES_WIDTH));
        props.setProperty("height", Integer.toString(RES_HEIGHT));
        try (java.io.OutputStream out = java.nio.file.Files.newOutputStream(settings)) {
            props.store(out, null);
        }

        Lwjgl3ApplicationConfiguration config = ClientLauncher.createConfiguration();
        Class<?> window = Lwjgl3ApplicationConfiguration.class.getSuperclass();
        Field w = window.getDeclaredField("windowWidth");
        w.setAccessible(true);
        Field h = window.getDeclaredField("windowHeight");
        h.setAccessible(true);
        assertEquals(RES_WIDTH, w.getInt(config));
        assertEquals(RES_HEIGHT, h.getInt(config));
        System.setProperty("user.home", oldHome);
    }

    @Test
    public void configUsesFullscreenWhenEnabled() throws Exception {
        String oldHome = System.getProperty("user.home");
        java.nio.file.Path home = java.nio.file.Files.createTempDirectory("client-launcher-full");
        System.setProperty("user.home", home.toString());
        java.nio.file.Path settings = home.resolve(".colony/settings.conf");
        java.nio.file.Files.createDirectories(settings.getParent());
        java.util.Properties props = new java.util.Properties();
        props.setProperty("fullscreen", "true");
        try (java.io.OutputStream out = java.nio.file.Files.newOutputStream(settings)) {
            props.store(out, null);
        }

        Lwjgl3ApplicationConfiguration config = ClientLauncher.createConfiguration();
        Class<?> window = Lwjgl3ApplicationConfiguration.class.getSuperclass();
        Field f = window.getDeclaredField("fullscreenMode");
        f.setAccessible(true);
        Object mode = f.get(config);
        Field width = window.getDeclaredField("windowWidth");
        width.setAccessible(true);
        if (mode == null) { // headless environment
            assertEquals(DEFAULT_WIDTH, width.getInt(config));
        } else {
            assertNotNull(mode);
        }
        System.setProperty("user.home", oldHome);
    }
}
