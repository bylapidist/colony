package net.lapidist.colony.settings;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

import java.util.EnumMap;
import java.util.Map;

/**
 * Stores key mappings for game actions.
 */
public final class KeyBindings {
    private static final String PREFIX = "key.";

    private static final Map<KeyAction, Integer> DEFAULTS = Map.of(
            KeyAction.MOVE_UP, Input.Keys.W,
            KeyAction.MOVE_DOWN, Input.Keys.S,
            KeyAction.MOVE_LEFT, Input.Keys.A,
            KeyAction.MOVE_RIGHT, Input.Keys.D,
            KeyAction.GATHER, Input.Keys.H,
            KeyAction.BUILD, Input.Keys.B,
            KeyAction.REMOVE, Input.Keys.R,
            KeyAction.CHAT, Input.Keys.T,
            KeyAction.MINIMAP, Input.Keys.M,
            KeyAction.TOGGLE_CAMERA, Input.Keys.F
    );

    private final EnumMap<KeyAction, Integer> bindings = new EnumMap<>(KeyAction.class);

    public KeyBindings() {
        reset();
    }

    /** Load bindings from preferences using provided prefix. */
    public static KeyBindings load(final Preferences prefs) {
        KeyBindings kb = new KeyBindings();
        for (KeyAction action : KeyAction.values()) {
            int code = prefs.getInteger(PREFIX + action.name(), kb.getKey(action));
            kb.setKey(action, code);
        }
        return kb;
    }

    /** Save bindings to preferences. */
    public void save(final Preferences prefs) {
        for (KeyAction action : KeyAction.values()) {
            prefs.putInteger(PREFIX + action.name(), getKey(action));
        }
    }

    public void reset() {
        bindings.clear();
        bindings.putAll(DEFAULTS);
    }

    public int getKey(final KeyAction action) {
        return bindings.get(action);
    }

    public void setKey(final KeyAction action, final int keycode) {
        bindings.put(action, keycode);
    }
}
