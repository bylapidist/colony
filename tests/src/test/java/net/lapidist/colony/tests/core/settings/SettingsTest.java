package net.lapidist.colony.tests.core.settings;

import net.lapidist.colony.io.Paths;
import net.lapidist.colony.io.TestPathService;
import java.nio.file.Files;
import java.nio.file.Path;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.settings.KeyAction;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class SettingsTest {
    private static final int RES_W = 1024;
    private static final int RES_H = 768;
    private static final float SCALE = 1.5f;
    private static final float SOFTNESS = 5f;

    @Test
    public void savesAndLoadsLocale() throws IOException {
        Path dir = Files.createTempDirectory("settings-test");
        Paths paths = new Paths(new TestPathService(dir));

        Settings settings = new Settings();
        settings.setLocale(Locale.GERMAN);
        settings.save(paths);

        Settings loaded = Settings.load(paths);
        assertEquals(Locale.GERMAN.getLanguage(), loaded.getLocale().getLanguage());
    }

    @Test
    public void defaultsWhenNoPreferences() throws IOException {
        Path dir = Files.createTempDirectory("settings-test-default");
        Paths paths = new Paths(new TestPathService(dir));

        Settings loaded = Settings.load(paths);
        assertEquals(Locale.getDefault().getLanguage(), loaded.getLocale().getLanguage());
    }

    @Test
    public void savesAndLoadsKeybindings() throws IOException {
        Path dir = Files.createTempDirectory("settings-test-key");
        Paths paths = new Paths(new TestPathService(dir));

        Settings settings = new Settings();
        settings.getKeyBindings().setKey(KeyAction.GATHER, com.badlogic.gdx.Input.Keys.G);
        settings.save(paths);

        Settings loaded = Settings.load(paths);
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
        Path dir = Files.createTempDirectory("settings-test-gfx");
        Paths paths = new Paths(new TestPathService(dir));

        Settings settings = new Settings();
        GraphicsSettings graphics = settings.getGraphicsSettings();
        graphics.setAntialiasingEnabled(true);
        graphics.setMipMapsEnabled(true);
        graphics.setRenderer("model");
        graphics.setSpriteCacheEnabled(false);
        graphics.setLightingEnabled(false);
        graphics.setNormalMapsEnabled(true);
        graphics.setSpecularMapsEnabled(true);
        graphics.setDayNightCycleEnabled(false);
        final int rays = 20;
        graphics.setLightRays(rays);
        graphics.setSoftShadowsEnabled(true);
        graphics.setShadowSoftnessLength(SOFTNESS);
        settings.save(paths);

        Settings loaded = Settings.load(paths);
        assertEquals(true, loaded.getGraphicsSettings().isAntialiasingEnabled());
        assertEquals(true, loaded.getGraphicsSettings().isMipMapsEnabled());
        assertEquals(true, loaded.getGraphicsSettings().isAnisotropicFilteringEnabled());
        assertEquals("model", loaded.getGraphicsSettings().getRenderer());
        assertEquals(false, loaded.getGraphicsSettings().isSpriteCacheEnabled());
        assertEquals(false, loaded.getGraphicsSettings().isLightingEnabled());
        assertEquals(true, loaded.getGraphicsSettings().isNormalMapsEnabled());
        assertEquals(true, loaded.getGraphicsSettings().isSpecularMapsEnabled());
        assertEquals(false, loaded.getGraphicsSettings().isDayNightCycleEnabled());
        assertEquals(rays, loaded.getGraphicsSettings().getLightRays());
        assertTrue(loaded.getGraphicsSettings().isSoftShadowsEnabled());
        assertEquals(SOFTNESS, loaded.getGraphicsSettings().getShadowSoftnessLength(), 0f);
    }

    @Test
    public void savesAndLoadsWindowSettings() throws IOException {
        Path dir = Files.createTempDirectory("settings-test-window");
        Paths paths = new Paths(new TestPathService(dir));

        Settings settings = new Settings();
        settings.setWidth(RES_W);
        settings.setHeight(RES_H);
        settings.setFullscreen(true);
        settings.setUiScale(SCALE);
        settings.save(paths);

        Settings loaded = Settings.load(paths);
        assertEquals(RES_W, loaded.getWidth());
        assertEquals(RES_H, loaded.getHeight());
        assertEquals(true, loaded.isFullscreen());
        assertEquals(SCALE, loaded.getUiScale(), 0f);
    }
}
