package net.lapidist.colony.tests.core.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class SettingsTest {

    @Test
    public void savesAndLoadsLocale() throws IOException {
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.clear();
        prefs.flush();

        Settings settings = new Settings();
        settings.setLocale(Locale.GERMAN);
        settings.save();

        Settings loaded = Settings.load();
        assertEquals(Locale.GERMAN.getLanguage(), loaded.getLocale().getLanguage());
    }

    @Test
    public void defaultsWhenNoPreferences() {
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.clear();
        prefs.flush();

        Settings loaded = Settings.load();
        assertEquals(Locale.getDefault().getLanguage(), loaded.getLocale().getLanguage());
    }

    @Test
    public void savesAndLoadsKeybindings() throws IOException {
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.clear();
        prefs.flush();

        Settings settings = new Settings();
        settings.getKeyBindings().setKey(KeyAction.GATHER, com.badlogic.gdx.Input.Keys.G);
        settings.save();

        Settings loaded = Settings.load();
        assertEquals(com.badlogic.gdx.Input.Keys.G, loaded.getKeyBindings().getKey(KeyAction.GATHER));
    }

    @Test
    public void resetRestoresDefaults() {
        Settings settings = new Settings();
        settings.getKeyBindings().setKey(KeyAction.MOVE_UP, com.badlogic.gdx.Input.Keys.Z);
        settings.getKeyBindings().reset();
        assertEquals(com.badlogic.gdx.Input.Keys.W, settings.getKeyBindings().getKey(KeyAction.MOVE_UP));
    }

    @Test
    public void savesAndLoadsGraphicsSettings() throws IOException {
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.clear();
        prefs.flush();

        Settings settings = new Settings();
        GraphicsSettings graphics = settings.getGraphicsSettings();
        graphics.setAntialiasingEnabled(true);
        graphics.setMipMapsEnabled(true);
        graphics.setRenderer("model");
        graphics.setShaderPlugin("test");
        graphics.setSpriteCacheEnabled(false);
        settings.save();

        Settings loaded = Settings.load();
        assertEquals(true, loaded.getGraphicsSettings().isAntialiasingEnabled());
        assertEquals(true, loaded.getGraphicsSettings().isMipMapsEnabled());
        assertEquals(true, loaded.getGraphicsSettings().isAnisotropicFilteringEnabled());
        assertEquals("test", loaded.getGraphicsSettings().getShaderPlugin());
        assertEquals("model", loaded.getGraphicsSettings().getRenderer());
        assertEquals(false, loaded.getGraphicsSettings().isSpriteCacheEnabled());
    }
}
