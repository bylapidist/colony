package net.lapidist.colony.settings;

import com.badlogic.gdx.Input;

import java.util.EnumMap;
import java.util.Map;

/**
 * Stores key mappings for game actions.
 */
public final class KeyBindings {
    private static final String PREFIX = "key.";

    private static final Map<KeyAction, Integer> DEFAULTS = Map.ofEntries(
            Map.entry(KeyAction.MOVE_UP, Input.Keys.W),
            Map.entry(KeyAction.MOVE_DOWN, Input.Keys.S),
            Map.entry(KeyAction.MOVE_LEFT, Input.Keys.A),
            Map.entry(KeyAction.MOVE_RIGHT, Input.Keys.D),
            Map.entry(KeyAction.GATHER, Input.Keys.H),
            Map.entry(KeyAction.BUILD, Input.Keys.B),
            Map.entry(KeyAction.REMOVE, Input.Keys.R),
            Map.entry(KeyAction.CHAT, Input.Keys.T),
            Map.entry(KeyAction.MINIMAP, Input.Keys.M),
            Map.entry(KeyAction.MENU, Input.Keys.ESCAPE),
            Map.entry(KeyAction.TOGGLE_CAMERA, Input.Keys.F),
            Map.entry(KeyAction.SELECT, Input.Keys.ENTER),
            Map.entry(KeyAction.SELECT_WOOD, Input.Keys.NUM_1),
            Map.entry(KeyAction.SELECT_STONE, Input.Keys.NUM_2),
            Map.entry(KeyAction.SELECT_FOOD, Input.Keys.NUM_3)
    );

    private final EnumMap<KeyAction, Integer> bindings = new EnumMap<>(KeyAction.class);

    public KeyBindings() {
        reset();
    }

    /** Load bindings from a properties object using provided prefix. */
    public static KeyBindings load(final java.util.Properties props) {
        KeyBindings kb = new KeyBindings();
        for (KeyAction action : KeyAction.values()) {
            String value = props.getProperty(PREFIX + action.name());
            if (value != null) {
                kb.setKey(action, Integer.parseInt(value));
            }
        }
        return kb;
    }

    /** Save bindings to a properties object. */
    public void save(final java.util.Properties props) {
        for (KeyAction action : KeyAction.values()) {
            props.setProperty(PREFIX + action.name(), Integer.toString(getKey(action)));
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
