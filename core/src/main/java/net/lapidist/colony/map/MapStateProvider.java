package net.lapidist.colony.map;

import net.lapidist.colony.components.state.map.MapState;

/**
 * Provides {@link MapState} instances for initializing the game world.
 */
@FunctionalInterface
public interface MapStateProvider {
    /**
     * Returns a map state to populate the world with.
     *
     * @return map state instance
     */
    MapState provide();
}
