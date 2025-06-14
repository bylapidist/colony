package net.lapidist.colony.tests.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.clear();
        prefs.putBoolean("graphics.antialiasing", true);
        prefs.flush();

        Lwjgl3ApplicationConfiguration config = ClientLauncher.createConfiguration();
        Field samples = Lwjgl3ApplicationConfiguration.class.getDeclaredField("samples");
        samples.setAccessible(true);
        assertEquals(ENABLED_SAMPLES, samples.getInt(config));
    }

    @Test
    public void configDisablesAntiAliasingByDefault() throws Exception {
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.clear();
        prefs.flush();

        Lwjgl3ApplicationConfiguration config = ClientLauncher.createConfiguration();
        Field samples = Lwjgl3ApplicationConfiguration.class.getDeclaredField("samples");
        samples.setAccessible(true);
        assertEquals(ENABLED_SAMPLES, samples.getInt(config));
    }
}
