package net.lapidist.colony.tests.systems;

import com.artemis.Aspect;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.IntBag;
import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.map.GeneratedMapStateProvider;
import net.lapidist.colony.map.ProvidedMapStateProvider;
import net.lapidist.colony.map.ChunkedMapGenerator;
import net.lapidist.colony.map.MapFactory;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.resources.ResourceComponent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapInitSystemTest {

    private static final int WOOD = 1;
    private static final int STONE = 2;
    private static final int FOOD = 3;

    @Test
    public void loadsProvidedStateIntoWorld() {
        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .passable(true)
                .build();
        state.putTile(tile);

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapInitSystem(new ProvidedMapStateProvider(state)))
                .build());
        world.process();

        IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        assertEquals(1, maps.size());
        MapComponent map = world.getMapper(MapComponent.class).get(maps.get(0));
        assertEquals(
                state.width() * state.height(),
                map.getTileMap().size()
        );
    }

    @Test
    public void generatesStateWithGenerator() {
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapInitSystem(new GeneratedMapStateProvider(new ChunkedMapGenerator(), 1, 1)))
                .build());
        world.process();

        IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        assertEquals(1, maps.size());
    }

    @Test
    public void mapFactoryCreatesLogicalComponents() {
        new net.lapidist.colony.base.BaseDefinitionsMod().init();
        MapState state = new MapState();
        state.putTile(TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .passable(true)
                .resources(new net.lapidist.colony.components.state.ResourceData(WOOD, STONE, FOOD))
                .build());

        World world = new World(new WorldConfigurationBuilder().build());
        var map = MapFactory.create(world, state).getComponent(MapComponent.class);
        var tile = map.getTiles().get(0);

        TileComponent tc = world.getMapper(TileComponent.class).get(tile);
        ResourceComponent rc = world.getMapper(ResourceComponent.class).get(tile);

        assertEquals("GRASS", tc.getTileType());
        assertEquals(WOOD, rc.getWood());
        assertEquals(STONE, rc.getStone());
        assertEquals(FOOD, rc.getFood());
    }
}
