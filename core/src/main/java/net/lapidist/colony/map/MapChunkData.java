package net.lapidist.colony.map;

import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.serialization.KryoType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Stores tile data for a 32x32 region of the map.
 * Tiles are generated on demand using {@link PerlinNoise}.
 */
@KryoType
public final class MapChunkData {
    public static final int CHUNK_SIZE = 32;
    private static final double NOISE_SCALE = 0.1;

    private final Map<TilePos, TileData> tiles = new HashMap<>();
    private final PerlinNoise noise;

    public MapChunkData() {
        this(new Random().nextLong());
    }

    public MapChunkData(final long seed) {
        this.noise = new PerlinNoise(seed);
    }

    /**
     * Returns the tile at the given local coordinates, generating it if needed.
     *
     * @param localX x coordinate within the chunk
     * @param localY y coordinate within the chunk
     * @return tile data instance
     */
    public TileData getTile(final int localX, final int localY) {
        TilePos pos = new TilePos(localX, localY);
        return tiles.computeIfAbsent(pos, p -> createTile(p.x(), p.y()));
    }

    private TileData createTile(final int x, final int y) {
        double value = noise.noise(x * NOISE_SCALE, y * NOISE_SCALE);
        String type = value > 0 ? "GRASS" : "DIRT";
        return TileData.builder()
                .x(x)
                .y(y)
                .tileType(type)
                .passable(true)
                .build();
    }

    /**
     * @return true when all tiles in this chunk have been generated
     */
    public boolean isGenerated() {
        return tiles.size() == CHUNK_SIZE * CHUNK_SIZE;
    }

    public Map<TilePos, TileData> getTiles() {
        return tiles;
    }
}
