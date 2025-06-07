package net.lapidist.colony.map;

import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;

import java.util.Random;

/**
 * Utility class for generating {@link MapState} instances with default data.
 */
public final class MapGenerator {

    private static final String[] TEXTURES = {"grass0", "dirt0"};
    private static final int NAME_RANGE = 100000;

    private MapGenerator() {
    }

    /**
     * Generate a {@link MapState} with tiles and a single building.
     *
     * @param width  map width in tiles
     * @param height map height in tiles
     * @return generated map state
     */
    public static MapState generate(final int width, final int height) {
        MapState state = new MapState();
        Random random = new Random();
        state.setName("map-" + random.nextInt(NAME_RANGE));
        state.setDescription("Generated map");

        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                TileData tile = new TileData();
                tile.setX(x);
                tile.setY(y);
                tile.setTileType("GRASS");
                tile.setTextureRef(TEXTURES[random.nextInt(TEXTURES.length)]);
                tile.setPassable(true);
                tile.setSelected(false);
                state.getTiles().add(tile);
            }
        }

        BuildingData building = new BuildingData();
        building.setX(1);
        building.setY(1);
        building.setBuildingType("HOUSE");
        building.setTextureRef("house0");
        state.getBuildings().add(building);

        return state;
    }
}
