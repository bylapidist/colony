package net.lapidist.colony.map;

import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.serialization.KryoType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

/**
 * Stores tile data for a 32x32 region of the map.
 * Tiles are generated on demand using {@link PerlinNoise}.
 */
@KryoType
public final class MapChunkData {
    public static final int CHUNK_SIZE = 32;
    private static final double NOISE_SCALE = 0.1;
    private static final int DEFAULT_WOOD = 10;
    private static final int DEFAULT_STONE = 5;
    private static final int DEFAULT_FOOD = 3;

    private final Map<TilePos, TileData> tiles = new ConcurrentHashMap<>();
    private final long seed;
    private transient PerlinNoise noise;
    private final int baseX;
    private final int baseY;

    public MapChunkData() {
        this(new Random().nextLong(), 0, 0);
    }

    public MapChunkData(final long seedValue) {
        this(seedValue, 0, 0);
    }

    public MapChunkData(final int chunkX, final int chunkY) {
        this(new Random().nextLong(), chunkX, chunkY);
    }

    public MapChunkData(final long seedValue, final int chunkX, final int chunkY) {
        this.seed = seedValue;
        this.noise = new PerlinNoise(seedValue);
        this.baseX = chunkX * CHUNK_SIZE;
        this.baseY = chunkY * CHUNK_SIZE;
    }

    private PerlinNoise noise() {
        if (noise == null) {
            noise = new PerlinNoise(seed);
        }
        return noise;
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
        int worldX = baseX + x;
        int worldY = baseY + y;
        double value = noise().noise(worldX * NOISE_SCALE, worldY * NOISE_SCALE);
        String type = value > 0 ? "GRASS" : "DIRT";
        return TileData.builder()
                .x(worldX)
                .y(worldY)
                .tileType(type)
                .passable(true)
                .resources(new net.lapidist.colony.components.state.ResourceData(
                        DEFAULT_WOOD, DEFAULT_STONE, DEFAULT_FOOD))
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

    public int chunkX() {
        return baseX / CHUNK_SIZE;
    }

    public int chunkY() {
        return baseY / CHUNK_SIZE;
    }
}
