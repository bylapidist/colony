package net.lapidist.colony.tests.client.render.data;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.map.MapFactory;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.MapRenderDataBuilder;
import net.lapidist.colony.client.render.SimpleMapRenderData;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.map.BuildingData;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.map.TileData;
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
        new net.lapidist.colony.base.BaseDefinitionsMod().init();
        MapState state = new MapState();
        state.putTile(TileData.builder()
                .x(TILE_X).y(TILE_Y).tileType("GRASS").passable(true)
                .resources(new net.lapidist.colony.components.state.resources.ResourceData(WOOD, STONE, FOOD))
                .build());
        state.buildings().add(new BuildingData(BUILDING_X, BUILDING_Y, "house"));

        World world = new World(new WorldConfigurationBuilder().build());
        MapComponent map = MapFactory.create(world, state).getComponent(MapComponent.class);

        MapRenderData data = MapRenderDataBuilder.fromMap(map, world);
        assertEquals(
                state.width() * state.height(),
                data.getTiles().size
        );
        assertEquals(1, data.getBuildings().size);

        var tile = data.getTiles().first();
        assertEquals("grass", tile.getTileType());
        assertEquals(1, tile.getWood());
        assertEquals(BUILDING_X, data.getBuildings().first().getX());
        assertEquals(tile, data.getTile(TILE_X, TILE_Y));
    }

    @Test
    public void updatesExistingRenderData() {
        new net.lapidist.colony.base.BaseDefinitionsMod().init();
        MapState state = new MapState();
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .build());

        World world = new World(new WorldConfigurationBuilder().build());
        MapComponent map = MapFactory.create(world, state).getComponent(MapComponent.class);

        SimpleMapRenderData data = (SimpleMapRenderData) MapRenderDataBuilder.fromMap(map, world);

        var tileEntity = map.getTiles().first();
        world.getMapper(net.lapidist.colony.components.maps.TileComponent.class)
                .get(tileEntity).setSelected(true);

        MapRenderDataBuilder.update(map, world, data);

        assertTrue(data.getTiles().first().isSelected());
    }
}
