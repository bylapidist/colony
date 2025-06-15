package net.lapidist.colony.map;

import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.components.state.ChunkPos;
import net.lapidist.colony.util.I18n;

import java.util.Map;
import java.util.Random;

/**
 * {@link MapGenerator} implementation that creates {@link MapChunkData} regions
 * and generates tiles lazily when requested.
 */
public final class ChunkedMapGenerator implements MapGenerator {

    private static final int NAME_RANGE = 100000;
    private static final int DEFAULT_WOOD = 10;
    private static final int DEFAULT_STONE = 5;
    private static final int DEFAULT_FOOD = 3;

    @Override
    public MapState generate(final int width, final int height) {
        MapState state = new MapState();
        Random random = new Random();
        long seed = random.nextLong();
        state = state.toBuilder()
                .name("map-" + random.nextInt(NAME_RANGE))
                .description(I18n.get("generator.generatedMap"))
                .width(width)
                .height(height)
                .build();

        int chunkWidth = (int) Math.ceil(width / (double) MapChunkData.CHUNK_SIZE);
        int chunkHeight = (int) Math.ceil(height / (double) MapChunkData.CHUNK_SIZE);
        for (int x = 0; x < chunkWidth; x++) {
            for (int y = 0; y < chunkHeight; y++) {
                state.chunks().put(new ChunkPos(x, y), new MapChunkData(seed, x, y));
            }
        }

        // generate a starting building in the middle
        BuildingData building = new BuildingData(width / 2, height / 2, "house");
        state.buildings().add(building);

        // prepopulate resources when tiles are first accessed
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                TileData td = state.getTile(x, y);
                TileData updated = td.toBuilder()
                        .resources(new ResourceData(new java.util.HashMap<>(Map.of(
                                "WOOD", DEFAULT_WOOD,
                                "STONE", DEFAULT_STONE,
                                "FOOD", DEFAULT_FOOD
                        ))))
                        .build();
                int chunkX = Math.floorDiv(x, MapChunkData.CHUNK_SIZE);
                int chunkY = Math.floorDiv(y, MapChunkData.CHUNK_SIZE);
                int localX = Math.floorMod(x, MapChunkData.CHUNK_SIZE);
                int localY = Math.floorMod(y, MapChunkData.CHUNK_SIZE);
                state.getOrCreateChunk(chunkX, chunkY)
                        .getTiles()
                        .put(new TilePos(localX, localY), updated);
            }
        }

        state = state.toBuilder()
                .playerResources(new ResourceData())
                .build();
        return state;
    }
}
