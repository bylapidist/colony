package net.lapidist.colony.mod;

/**
 * Extension point for additional game functionality.
 */
public interface GameMod {

    /**
     * Called after the mod has been loaded.
     */
    default void init() {
    }

    /**
     * Called when the mod is unloaded.
     */
    default void dispose() {
    }
}
