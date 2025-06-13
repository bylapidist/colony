package net.lapidist.colony.map.chunk;

import net.lapidist.colony.components.state.MapChunk;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.map.PerlinNoise;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Chunk generator using Perlin noise for terrain.
 */
public final class PerlinChunkGenerator implements ChunkGenerator {

    private static final double NOISE_SCALE = 0.1;
    private final PerlinNoise noise;

    public PerlinChunkGenerator() {
        this.noise = new PerlinNoise(new Random().nextLong());
    }

    @Override
    public MapChunk generate(final int chunkX, final int chunkY, final int size) {
        Map<TilePos, TileData> tiles = new HashMap<>(size * size);
        int startX = chunkX * size;
        int startY = chunkY * size;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int worldX = startX + x;
                int worldY = startY + y;
                String type = noise.noise(worldX * NOISE_SCALE, worldY * NOISE_SCALE) > 0 ? "GRASS" : "DIRT";
                tiles.put(new TilePos(worldX, worldY), TileData.builder()
                        .x(worldX)
                        .y(worldY)
                        .tileType(type)
                        .passable(true)
                        .build());
            }
        }
        return new MapChunk(chunkX, chunkY, tiles);
    }
}
