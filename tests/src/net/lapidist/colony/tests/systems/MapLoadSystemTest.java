package net.lapidist.colony.tests.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.IntBag;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapLoadSystemTest {

    @Test
    public void loadsMapStateIntoWorld() {
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

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state))
                .build());

        world.process();

        IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        assertEquals(1, maps.size());

        Entity map = world.getEntity(maps.get(0));
        MapComponent mapComponent = world.getMapper(MapComponent.class).get(map);
        assertEquals(1, mapComponent.getTiles().size);
        assertEquals(1, mapComponent.getEntities().size);
    }
}
