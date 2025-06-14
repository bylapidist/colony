package net.lapidist.colony.tests.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.IntBag;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.client.systems.network.BuildingUpdateSystem;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class BuildingUpdateSystemTest {

    @Test
    public void appliesServerBuildingPlacement() {
        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .passable(true)
                .build();
        state.putTile(tile);

        GameClient client = new GameClient();
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new BuildingUpdateSystem(client))
                .build());

        world.process();

        BuildingData data = new BuildingData(0, 0, "house");
        client.injectBuildingUpdate(data);

        world.process();

        IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        Entity map = world.getEntity(maps.get(0));
        MapComponent mapComponent = world.getMapper(MapComponent.class).get(map);
        Entity building = mapComponent.getEntities().get(0);
        BuildingComponent bc = world.getMapper(BuildingComponent.class).get(building);

        assertEquals(0, bc.getX());
        assertEquals(0, bc.getY());
    }
}
