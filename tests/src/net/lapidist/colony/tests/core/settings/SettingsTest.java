package net.lapidist.colony.tests.core.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.settings.KeyAction;
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
    public void savesAndLoadsKeybinds() throws IOException {
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.clear();
        prefs.flush();

        Settings settings = new Settings();
        settings.setKey(KeyAction.MOVE_UP, com.badlogic.gdx.Input.Keys.Z);
        settings.save();

        Settings loaded = Settings.load();
        assertEquals(com.badlogic.gdx.Input.Keys.Z, loaded.getKey(KeyAction.MOVE_UP));
    }

    @Test
    public void resetRestoresDefaults() {
        Settings settings = new Settings();
        settings.setKey(KeyAction.MOVE_UP, com.badlogic.gdx.Input.Keys.Z);
        settings.resetKeybinds();
        assertEquals(com.badlogic.gdx.Input.Keys.W, settings.getKey(KeyAction.MOVE_UP));
    }

    @Test
    public void defaultsWhenNoPreferences() {
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.clear();
        prefs.flush();

        Settings loaded = Settings.load();
        assertEquals(Locale.getDefault().getLanguage(), loaded.getLocale().getLanguage());
    }
}
