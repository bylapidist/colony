package net.lapidist.colony.map;

import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.i18n.I18n;

import java.util.Random;

/**
 * Default {@link MapGenerator} implementation generating a simple random map
 * using {@link PerlinNoise} to vary terrain textures.
 */
public final class DefaultMapGenerator implements MapGenerator {

    private static final String[] TEXTURES = {"grass0", "dirt0"};
    private static final double NOISE_SCALE = 0.1;
    private static final int NAME_RANGE = 100000;
    private static final int DEFAULT_WOOD = 10;
    private static final int DEFAULT_STONE = 5;
    private static final int DEFAULT_FOOD = 3;

    @Override
    public MapState generate(final int width, final int height) {
        MapState state = new MapState();
        Random random = new Random();
        PerlinNoise noise = new PerlinNoise(random.nextLong());
        state = state.toBuilder()
                .name("map-" + random.nextInt(NAME_RANGE))
                .description(I18n.get("generator.generatedMap"))
                .build();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                state.tiles().put(new TilePos(x, y), createTile(x, y, random, noise));
            }
        }

        BuildingData building = new BuildingData(width / 2, height / 2, "HOUSE", "house0");
        state.buildings().add(building);

        state = state.toBuilder()
                .playerResources(new ResourceData())
                .build();

        return state;
    }

    private static TileData createTile(
            final int x,
            final int y,
            final Random random,
            final PerlinNoise noise
    ) {
        double value = noise.noise(x * NOISE_SCALE, y * NOISE_SCALE);
        String texture = value > 0 ? TEXTURES[0] : TEXTURES[1];
        return TileData.builder()
                .x(x)
                .y(y)
                .tileType("GRASS")
                .textureRef(texture)
                .passable(true)
                .selected(false)
                .resources(new ResourceData(DEFAULT_WOOD, DEFAULT_STONE, DEFAULT_FOOD))
                .build();
    }
}
