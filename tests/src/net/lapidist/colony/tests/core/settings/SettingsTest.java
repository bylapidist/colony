package net.lapidist.colony.tests.core.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import net.lapidist.colony.settings.Settings;
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
    public void savesAndLoadsKeybinds() throws IOException {
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.clear();
        prefs.flush();

        Settings settings = new Settings();
        settings.setUpKey(com.badlogic.gdx.Input.Keys.Z);
        settings.setDownKey(com.badlogic.gdx.Input.Keys.X);
        settings.setLeftKey(com.badlogic.gdx.Input.Keys.C);
        settings.setRightKey(com.badlogic.gdx.Input.Keys.V);
        settings.setGatherKey(com.badlogic.gdx.Input.Keys.B);
        settings.save();

        Settings loaded = Settings.load();
        assertEquals(com.badlogic.gdx.Input.Keys.Z, loaded.getUpKey());
        assertEquals(com.badlogic.gdx.Input.Keys.X, loaded.getDownKey());
        assertEquals(com.badlogic.gdx.Input.Keys.C, loaded.getLeftKey());
        assertEquals(com.badlogic.gdx.Input.Keys.V, loaded.getRightKey());
        assertEquals(com.badlogic.gdx.Input.Keys.B, loaded.getGatherKey());
    }

    @Test
    public void defaultKeybindsWhenNoPreferences() {
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.clear();
        prefs.flush();

        Settings loaded = Settings.load();
        assertEquals(com.badlogic.gdx.Input.Keys.W, loaded.getUpKey());
        assertEquals(com.badlogic.gdx.Input.Keys.S, loaded.getDownKey());
        assertEquals(com.badlogic.gdx.Input.Keys.A, loaded.getLeftKey());
        assertEquals(com.badlogic.gdx.Input.Keys.D, loaded.getRightKey());
        assertEquals(com.badlogic.gdx.Input.Keys.H, loaded.getGatherKey());
    }
}
