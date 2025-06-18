package net.lapidist.colony.map;

import net.lapidist.colony.components.state.map.MapState;

/**
 * Strategy interface for generating {@link MapState} instances.
 */
public interface MapGenerator {
    /**
     * Generate a map state using the given dimensions.
     *
     * @param width  map width in tiles
     * @param height map height in tiles
     * @return generated map state
     */
    MapState generate(int width, int height);
}
