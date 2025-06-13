package net.lapidist.colony.map.chunk;

import net.lapidist.colony.components.state.MapChunk;

/**
 * Generates chunks of map data for infinite world generation.
 */
public interface ChunkGenerator {
    /**
     * Generate a chunk at the given chunk coordinates.
     *
     * @param chunkX chunk x coordinate
     * @param chunkY chunk y coordinate
     * @param size   chunk size in tiles
     * @return generated chunk data
     */
    MapChunk generate(int chunkX, int chunkY, int size);
}
