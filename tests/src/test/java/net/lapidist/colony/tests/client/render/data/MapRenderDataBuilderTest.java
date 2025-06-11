package net.lapidist.colony.tests.client.render.data;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.map.MapFactory;
import net.lapidist.colony.render.MapRenderData;
import net.lapidist.colony.render.MapRenderDataBuilder;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapRenderDataBuilderTest {
    private static final int TILE_X = 0;
    private static final int TILE_Y = 0;
    private static final int BUILDING_X = 1;
    private static final int BUILDING_Y = 1;
    private static final int WOOD = 1;
    private static final int STONE = 2;
    private static final int FOOD = 3;

    @Test
    public void convertsMapToRenderObjects() {
        MapState state = new MapState();
        state.tiles().put(new TilePos(TILE_X, TILE_Y), TileData.builder()
                .x(TILE_X).y(TILE_Y).tileType("GRASS").passable(true)
                .resources(new net.lapidist.colony.components.state.ResourceData(WOOD, STONE, FOOD))
                .build());
        state.buildings().add(new BuildingData(BUILDING_X, BUILDING_Y, "HOUSE"));

        World world = new World(new WorldConfigurationBuilder().build());
        MapComponent map = MapFactory.create(world, state).getComponent(MapComponent.class);

        MapRenderData data = MapRenderDataBuilder.fromMap(map, world);
        assertEquals(1, data.getTiles().size);
        assertEquals(1, data.getBuildings().size);

        var tile = data.getTiles().first();
        assertEquals("grass", tile.getTileType());
        assertEquals(1, tile.getWood());
        assertEquals(BUILDING_X, data.getBuildings().first().getX());
    }
}
