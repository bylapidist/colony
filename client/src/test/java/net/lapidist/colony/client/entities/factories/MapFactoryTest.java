package net.lapidist.colony.client.entities.factories;

import com.artemis.Entity;
import com.artemis.World;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapFactoryTest {

    @Test
    public void createsMapWithTilesAndBuildings() {
        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .textureRef("grass0")
                .passable(true)
                .build();
        state.tiles().put(new TilePos(0, 0), tile);

        BuildingData building = new BuildingData(1, 1, "HOUSE", "house0");
        state.buildings().add(building);

        World world = new World();
        Entity mapEntity = MapFactory.create(world, state);

        MapComponent map = world.getMapper(MapComponent.class).get(mapEntity);
        assertEquals(1, map.getTiles().size);
        assertEquals(1, map.getTileMap().size());
        assertEquals(1, map.getEntities().size);

        TileComponent tileComponent = world.getMapper(TileComponent.class)
                .get(map.getTiles().get(0));
        assertEquals(TileComponent.TileType.GRASS, tileComponent.getTileType());

        BuildingComponent bc = world.getMapper(BuildingComponent.class)
                .get(map.getEntities().get(0));
        assertEquals(BuildingComponent.BuildingType.HOUSE, bc.getBuildingType());
    }
}
