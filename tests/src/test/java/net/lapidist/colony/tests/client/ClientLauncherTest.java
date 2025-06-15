package net.lapidist.colony.tests.client;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import net.lapidist.colony.client.ClientLauncher;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class ClientLauncherTest {

    private static final int ENABLED_SAMPLES = 4;

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
}
