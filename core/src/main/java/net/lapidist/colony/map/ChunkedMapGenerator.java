package net.lapidist.colony.map;

import net.lapidist.colony.components.state.map.BuildingData;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.resources.ResourceData;
import net.lapidist.colony.components.state.map.ChunkPos;
import net.lapidist.colony.util.I18n;

import java.util.Random;

/**
 * {@link MapGenerator} implementation that creates {@link MapChunkData} regions
 * and generates tiles lazily when requested.
 */
public final class ChunkedMapGenerator implements MapGenerator {

    private static final int NAME_RANGE = 100000;

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

        state = state.toBuilder()
                .playerResources(new ResourceData())
                .build();
        return state;
    }
}
