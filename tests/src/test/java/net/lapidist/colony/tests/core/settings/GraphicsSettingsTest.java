package net.lapidist.colony.tests.core.settings;

import java.util.Properties;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class GraphicsSettingsTest {

    private static final float SOFTNESS = 3f;

    @Test
    public void savesAndLoadsValues() {
        Properties props = new Properties();

        GraphicsSettings gs = new GraphicsSettings();
        final int rays = 32;
        gs.setAntialiasingEnabled(true);
        gs.setMipMapsEnabled(true);
        gs.setAnisotropicFilteringEnabled(true);
        gs.setRenderer("model");
        gs.setSpriteCacheEnabled(false);
        gs.setLightingEnabled(false);
        gs.setNormalMapsEnabled(true);
        gs.setSpecularMapsEnabled(true);
        gs.setDayNightCycleEnabled(false);
        gs.setLightRays(rays);
        gs.setSoftShadowsEnabled(true);
        gs.setShadowSoftnessLength(SOFTNESS);
        gs.save(props);

        GraphicsSettings loaded = GraphicsSettings.load(props);
        assertTrue(loaded.isAntialiasingEnabled());
        assertTrue(loaded.isMipMapsEnabled());
        assertTrue(loaded.isAnisotropicFilteringEnabled());
        assertEquals("model", loaded.getRenderer());
        assertFalse(loaded.isSpriteCacheEnabled());
        assertFalse(loaded.isLightingEnabled());
        assertTrue(loaded.isNormalMapsEnabled());
        assertTrue(loaded.isSpecularMapsEnabled());
        assertFalse(loaded.isDayNightCycleEnabled());
        assertEquals(rays, loaded.getLightRays());
        assertTrue(loaded.isSoftShadowsEnabled());
        assertEquals(SOFTNESS, loaded.getShadowSoftnessLength(), 0f);
    }

    @Test
    public void defaultsWhenNoPreferences() {
        Properties props = new Properties();

        GraphicsSettings loaded = GraphicsSettings.load(props);
        assertTrue(loaded.isAntialiasingEnabled());
        assertTrue(loaded.isMipMapsEnabled());
        assertTrue(loaded.isAnisotropicFilteringEnabled());
        assertEquals("sprite", loaded.getRenderer());
        assertTrue(loaded.isSpriteCacheEnabled());
        assertTrue(loaded.isLightingEnabled());
        assertFalse(loaded.isNormalMapsEnabled());
        assertFalse(loaded.isSpecularMapsEnabled());
        assertTrue(loaded.isDayNightCycleEnabled());
        assertEquals(GraphicsSettings.load(new Properties()).getLightRays(), loaded.getLightRays());
        assertFalse(loaded.isSoftShadowsEnabled());
    }
}
