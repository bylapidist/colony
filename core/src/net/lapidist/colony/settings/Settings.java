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
    private static final String KEY_UP = "key.up";
    private static final String KEY_DOWN = "key.down";
    private static final String KEY_LEFT = "key.left";
    private static final String KEY_RIGHT = "key.right";
    private static final String KEY_GATHER = "key.gather";

    private Locale locale = Locale.getDefault();
    private int upKey = Input.Keys.W;
    private int downKey = Input.Keys.S;
    private int leftKey = Input.Keys.A;
    private int rightKey = Input.Keys.D;
    private int gatherKey = Input.Keys.H;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale localeToSet) {
        this.locale = localeToSet;
    }

    public int getUpKey() {
        return upKey;
    }

    public void setUpKey(final int key) {
        this.upKey = key;
    }

    public int getDownKey() {
        return downKey;
    }

    public void setDownKey(final int key) {
        this.downKey = key;
    }

    public int getLeftKey() {
        return leftKey;
    }

    public void setLeftKey(final int key) {
        this.leftKey = key;
    }

    public int getRightKey() {
        return rightKey;
    }

    public void setRightKey(final int key) {
        this.rightKey = key;
    }

    public int getGatherKey() {
        return gatherKey;
    }

    public void setGatherKey(final int key) {
        this.gatherKey = key;
    }

    public void resetKeybinds() {
        upKey = Input.Keys.W;
        downKey = Input.Keys.S;
        leftKey = Input.Keys.A;
        rightKey = Input.Keys.D;
        gatherKey = Input.Keys.H;
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
            settings.setUpKey(prefs.getInteger(KEY_UP, settings.getUpKey()));
            settings.setDownKey(prefs.getInteger(KEY_DOWN, settings.getDownKey()));
            settings.setLeftKey(prefs.getInteger(KEY_LEFT, settings.getLeftKey()));
            settings.setRightKey(prefs.getInteger(KEY_RIGHT, settings.getRightKey()));
            settings.setGatherKey(prefs.getInteger(KEY_GATHER, settings.getGatherKey()));
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
        prefs.putInteger(KEY_UP, upKey);
        prefs.putInteger(KEY_DOWN, downKey);
        prefs.putInteger(KEY_LEFT, leftKey);
        prefs.putInteger(KEY_RIGHT, rightKey);
        prefs.putInteger(KEY_GATHER, gatherKey);
        prefs.flush();
    }
}
