package net.lapidist.colony.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import net.lapidist.colony.i18n.I18n;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Locale;

/**
 * Simple POJO for user settings persisted to the game folder.
 */
public final class Settings {
    private static final String LANGUAGE_KEY = "language";
    private static final String KEYBIND_PREFIX = "keybind.";

    private Locale locale = Locale.getDefault();
    private final EnumMap<KeyAction, Integer> keybinds = new EnumMap<>(KeyAction.class);

    public Settings() {
        resetKeybinds();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale localeToSet) {
        this.locale = localeToSet;
    }

    public int getKey(final KeyAction action) {
        return keybinds.get(action);
    }

    public void setKey(final KeyAction action, final int keycode) {
        keybinds.put(action, keycode);
    }

    /** Reset all keybinds to their default values. */
    public void resetKeybinds() {
        keybinds.put(KeyAction.MOVE_UP, com.badlogic.gdx.Input.Keys.W);
        keybinds.put(KeyAction.MOVE_DOWN, com.badlogic.gdx.Input.Keys.S);
        keybinds.put(KeyAction.MOVE_LEFT, com.badlogic.gdx.Input.Keys.A);
        keybinds.put(KeyAction.MOVE_RIGHT, com.badlogic.gdx.Input.Keys.D);
        keybinds.put(KeyAction.GATHER, com.badlogic.gdx.Input.Keys.H);
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
            for (KeyAction action : KeyAction.values()) {
                String key = KEYBIND_PREFIX + action.name();
                int code = prefs.getInteger(key, -1);
                if (code != -1) {
                    settings.setKey(action, code);
                }
            }
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
        for (KeyAction action : KeyAction.values()) {
            prefs.putInteger(KEYBIND_PREFIX + action.name(), getKey(action));
        }
        prefs.flush();
    }
}
