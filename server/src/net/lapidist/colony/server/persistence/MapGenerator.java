package net.lapidist.colony.server.persistence;

import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;

import java.util.Random;

public final class MapGenerator {
    private MapGenerator() { }

    public static MapState generate() {
        MapState mapState = new MapState();
        Random random = new Random();
        String[] textures = new String[]{"grass0", "dirt0"};
        for (int x = 0; x <= GameConstants.MAP_WIDTH; x++) {
            for (int y = 0; y <= GameConstants.MAP_HEIGHT; y++) {
                TileData tile = new TileData();
                tile.setX(x);
                tile.setY(y);
                tile.setTileType("GRASS");
                tile.setTextureRef(textures[random.nextInt(textures.length)]);
                tile.setPassable(true);
                mapState.getTiles().add(tile);
            }
        }
        BuildingData building = new BuildingData();
        building.setX(1);
        building.setY(1);
        building.setBuildingType("HOUSE");
        building.setTextureRef("house0");
        mapState.getBuildings().add(building);
        return mapState;
    }
}
