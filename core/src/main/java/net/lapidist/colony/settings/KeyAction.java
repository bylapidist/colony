package net.lapidist.colony.settings;

/**
 * Logical game actions that can be bound to keyboard keys.
 */
public enum KeyAction {
    MOVE_UP("moveUp"),
    MOVE_DOWN("moveDown"),
    MOVE_LEFT("moveLeft"),
    MOVE_RIGHT("moveRight"),
    GATHER("gather"),
    BUILD("build"),
    REMOVE("remove"),
    CHAT("chat"),
    MINIMAP("minimap"),
    MENU("menu"),
    TOGGLE_CAMERA("toggleCamera"),
    SELECT("select"),
    SELECT_WOOD("selectWood"),
    SELECT_STONE("selectStone"),
    SELECT_FOOD("selectFood"),
    PAUSE("pause"),
    SLOW_MOTION("slowMotion");

    private final String i18nKey;

    KeyAction(final String key) {
        this.i18nKey = key;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}
