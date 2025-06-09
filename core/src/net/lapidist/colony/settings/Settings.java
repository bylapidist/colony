package net.lapidist.colony.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import net.lapidist.colony.i18n.I18n;

import java.io.IOException;
import java.util.Locale;

/**
 * Simple POJO for user settings persisted to the game folder.
 */
public final class Settings {
    private static final String LANGUAGE_KEY = "language";
    private static final String KEY_PREFIX = "key.";
    private static final String MOVE_UP_KEY = "moveUp";
    private static final String MOVE_DOWN_KEY = "moveDown";
    private static final String MOVE_LEFT_KEY = "moveLeft";
    private static final String MOVE_RIGHT_KEY = "moveRight";
    private static final String GATHER_KEY = "gather";

    private Locale locale = Locale.getDefault();

    public Settings() {
        resetKeybinds();
    }

    public enum Action {
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
        GATHER
    }

    private final java.util.EnumMap<Action, Integer> keybinds =
            new java.util.EnumMap<>(Action.class);

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale localeToSet) {
        this.locale = localeToSet;
    }

    public int getKey(final Action action) {
        return keybinds.get(action);
    }

    public void setKey(final Action action, final int keycode) {
        keybinds.put(action, keycode);
    }

    public void resetKeybinds() {
        keybinds.put(Action.MOVE_UP, Input.Keys.W);
        keybinds.put(Action.MOVE_DOWN, Input.Keys.S);
        keybinds.put(Action.MOVE_LEFT, Input.Keys.A);
        keybinds.put(Action.MOVE_RIGHT, Input.Keys.D);
        keybinds.put(Action.GATHER, Input.Keys.H);
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
            settings.setKey(Action.MOVE_UP,
                    prefs.getInteger(KEY_PREFIX + MOVE_UP_KEY, settings.getKey(Action.MOVE_UP)));
            settings.setKey(Action.MOVE_DOWN,
                    prefs.getInteger(KEY_PREFIX + MOVE_DOWN_KEY, settings.getKey(Action.MOVE_DOWN)));
            settings.setKey(Action.MOVE_LEFT,
                    prefs.getInteger(KEY_PREFIX + MOVE_LEFT_KEY, settings.getKey(Action.MOVE_LEFT)));
            settings.setKey(Action.MOVE_RIGHT,
                    prefs.getInteger(KEY_PREFIX + MOVE_RIGHT_KEY, settings.getKey(Action.MOVE_RIGHT)));
            settings.setKey(Action.GATHER,
                    prefs.getInteger(KEY_PREFIX + GATHER_KEY, settings.getKey(Action.GATHER)));
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
        prefs.putInteger(KEY_PREFIX + MOVE_UP_KEY, getKey(Action.MOVE_UP));
        prefs.putInteger(KEY_PREFIX + MOVE_DOWN_KEY, getKey(Action.MOVE_DOWN));
        prefs.putInteger(KEY_PREFIX + MOVE_LEFT_KEY, getKey(Action.MOVE_LEFT));
        prefs.putInteger(KEY_PREFIX + MOVE_RIGHT_KEY, getKey(Action.MOVE_RIGHT));
        prefs.putInteger(KEY_PREFIX + GATHER_KEY, getKey(Action.GATHER));
        prefs.flush();
    }
}
