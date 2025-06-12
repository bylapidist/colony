package net.lapidist.colony.tests.core.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class GraphicsSettingsTest {

    @Test
    public void savesAndLoadsValues() {
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.clear();
        prefs.flush();

        GraphicsSettings gs = new GraphicsSettings();
        gs.setAntialiasingEnabled(true);
        gs.setMipMapsEnabled(true);
        gs.setAnisotropicFilteringEnabled(true);
        gs.setShadersEnabled(true);
        gs.setRenderer("model");
        gs.setSpriteCacheEnabled(false);
        gs.save(prefs);
        prefs.flush();

        GraphicsSettings loaded = GraphicsSettings.load(prefs);
        assertTrue(loaded.isAntialiasingEnabled());
        assertTrue(loaded.isMipMapsEnabled());
        assertTrue(loaded.isAnisotropicFilteringEnabled());
        assertTrue(loaded.isShadersEnabled());
        assertEquals("model", loaded.getRenderer());
        assertFalse(loaded.isSpriteCacheEnabled());
    }

    @Test
    public void defaultsWhenNoPreferences() {
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.clear();
        prefs.flush();

        GraphicsSettings loaded = GraphicsSettings.load(prefs);
        assertFalse(loaded.isAntialiasingEnabled());
        assertFalse(loaded.isMipMapsEnabled());
        assertFalse(loaded.isAnisotropicFilteringEnabled());
        assertFalse(loaded.isShadersEnabled());
        assertEquals("sprite", loaded.getRenderer());
        assertTrue(loaded.isSpriteCacheEnabled());
    }
}
