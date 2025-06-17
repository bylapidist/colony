package net.lapidist.colony.settings;

import net.lapidist.colony.io.Paths;

import java.io.IOException;
import java.util.Locale;

/**
 * Simple POJO for user settings persisted to the game folder.
 */
public final class Settings {
    private static final String LANGUAGE_KEY = "language";

    private final KeyBindings keyBindings = new KeyBindings();
    private final GraphicsSettings graphicsSettings = new GraphicsSettings();

    private Locale locale = Locale.getDefault();

    public KeyBindings getKeyBindings() {
        return keyBindings;
    }

    public GraphicsSettings getGraphicsSettings() {
        return graphicsSettings;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale localeToSet) {
        this.locale = localeToSet;
    }

    /**
     * Load settings from the configuration file located next to the save folder.
     * Defaults are returned when the file does not exist or cannot be read.
     */
    public static Settings load() {
        return load(Paths.get());
    }

    public static Settings load(final Paths paths) {
        Settings settings = new Settings();
        try {
            java.nio.file.Path file = paths.getSettingsFile();
            java.util.Properties props = new java.util.Properties();
            if (java.nio.file.Files.exists(file)) {
                try (java.io.InputStream in = java.nio.file.Files.newInputStream(file)) {
                    props.load(in);
                }
            }
            String lang = props.getProperty(LANGUAGE_KEY);
            if (lang != null && !lang.isEmpty()) {
                settings.setLocale(java.util.Locale.forLanguageTag(lang));
            }
            KeyBindings loaded = KeyBindings.load(props);
            for (KeyAction action : KeyAction.values()) {
                settings.keyBindings.setKey(action, loaded.getKey(action));
            }
            GraphicsSettings gLoaded = GraphicsSettings.load(props);
            settings.graphicsSettings.setAntialiasingEnabled(gLoaded.isAntialiasingEnabled());
            settings.graphicsSettings.setMipMapsEnabled(gLoaded.isMipMapsEnabled());
            settings.graphicsSettings.setAnisotropicFilteringEnabled(
                    gLoaded.isAnisotropicFilteringEnabled());
            settings.graphicsSettings.setRenderer(gLoaded.getRenderer());
            settings.graphicsSettings.setSpriteCacheEnabled(gLoaded.isSpriteCacheEnabled());
            settings.graphicsSettings.setLightingEnabled(gLoaded.isLightingEnabled());
            settings.graphicsSettings.setNormalMapsEnabled(gLoaded.isNormalMapsEnabled());
            settings.graphicsSettings.setSpecularMapsEnabled(gLoaded.isSpecularMapsEnabled());
            settings.graphicsSettings.setNormalMapStrength(gLoaded.getNormalMapStrength());
            settings.graphicsSettings.setDayNightCycleEnabled(gLoaded.isDayNightCycleEnabled());
            settings.graphicsSettings.setLightRays(gLoaded.getLightRays());
        } catch (IOException e) {
            // ignore and use defaults
        }
        return settings;
    }

    /**
     * Persist settings to the configuration file next to the save folder.
     */
    public void save() throws IOException {
        save(Paths.get());
    }

    public void save(final Paths paths) throws IOException {
        java.nio.file.Path file = paths.getSettingsFile();
        java.util.Properties props = new java.util.Properties();
        if (java.nio.file.Files.exists(file)) {
            try (java.io.InputStream in = java.nio.file.Files.newInputStream(file)) {
                props.load(in);
            }
        }
        props.setProperty(LANGUAGE_KEY, locale.toLanguageTag());
        keyBindings.save(props);
        graphicsSettings.save(props);
        try (java.io.OutputStream out = java.nio.file.Files.newOutputStream(file)) {
            props.store(out, null);
        }
    }
}
