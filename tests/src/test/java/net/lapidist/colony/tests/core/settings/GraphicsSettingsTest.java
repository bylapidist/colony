package net.lapidist.colony.tests.core.settings;

import java.util.Properties;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class GraphicsSettingsTest {

    @Test
    public void savesAndLoadsValues() {
        Properties props = new Properties();

        GraphicsSettings gs = new GraphicsSettings();
        final int rays = 32;
        final float softness = 3f;
        gs.setAntialiasingEnabled(true);
        gs.setMipMapsEnabled(true);
        gs.setAnisotropicFilteringEnabled(true);
        gs.setRenderer("model");
        gs.setSpriteCacheEnabled(false);
        gs.setLightingEnabled(false);
        gs.setNormalMapsEnabled(true);
        gs.setSpecularMapsEnabled(true);
        gs.setSoftShadowsEnabled(false);
        gs.setShadowSoftnessLength(softness);
        gs.setDayNightCycleEnabled(false);
        gs.setLightRays(rays);
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
        assertFalse(loaded.isSoftShadowsEnabled());
        assertEquals(softness, loaded.getShadowSoftnessLength(), 0f);
        assertFalse(loaded.isDayNightCycleEnabled());
        assertEquals(rays, loaded.getLightRays());
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
        assertTrue(loaded.isSoftShadowsEnabled());
        assertEquals(GraphicsSettings.load(new Properties()).getShadowSoftnessLength(),
                loaded.getShadowSoftnessLength(), 0f);
        assertTrue(loaded.isDayNightCycleEnabled());
        assertEquals(GraphicsSettings.load(new Properties()).getLightRays(), loaded.getLightRays());
    }
}
