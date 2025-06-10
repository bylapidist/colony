package net.lapidist.colony.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import net.lapidist.colony.i18n.I18n;

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
     * Load settings from LibGDX preferences. If no preferences are present
     * defaults are returned.
     */
    public static Settings load() {
        Settings settings = new Settings();
        if (Gdx.app != null) {
            Preferences prefs = Gdx.app.getPreferences("settings");
            String lang = prefs.getString(LANGUAGE_KEY, "");
            if (!lang.isEmpty()) {
                settings.setLocale(Locale.forLanguageTag(lang));
            }
            KeyBindings loaded = KeyBindings.load(prefs);
            for (KeyAction action : KeyAction.values()) {
                settings.keyBindings.setKey(action, loaded.getKey(action));
            }
            GraphicsSettings gLoaded = GraphicsSettings.load(prefs);
            settings.graphicsSettings.setAntialiasingEnabled(gLoaded.isAntialiasingEnabled());
            settings.graphicsSettings.setMipMapsEnabled(gLoaded.isMipMapsEnabled());
            settings.graphicsSettings.setAnisotropicFilteringEnabled(
                    gLoaded.isAnisotropicFilteringEnabled());
            settings.graphicsSettings.setShadersEnabled(gLoaded.isShadersEnabled());
        }
        return settings;
    }

    /**
     * Persist settings using LibGDX preferences.
     */
    public void save() throws IOException {
        if (Gdx.app == null) {
            throw new IOException(I18n.get("error.preferencesUnavailable"));
        }
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.putString(LANGUAGE_KEY, locale.toLanguageTag());
        keyBindings.save(prefs);
        graphicsSettings.save(prefs);
        prefs.flush();
    }
}
