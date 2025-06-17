package net.lapidist.colony.tests.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.client.systems.network.ResourceUpdateSystem;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.components.resources.ResourceComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/** Tests for {@link ResourceUpdateSystem}. */
@RunWith(GdxTestRunner.class)
public class ResourceUpdateSystemTest {
    private static final int INITIAL_WOOD = 10;
    private static final int UPDATED_WOOD = 5;

    @Test
    public void appliesUpdatesAndAccumulatesResources() {
        MapState state = new MapState();
        ResourceData res = new ResourceData(INITIAL_WOOD, 0, 0);
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .passable(true)
                .resources(res)
                .build();
        state.putTile(tile);

        GameClient client = new GameClient();
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new MapRenderDataSystem(),
                        new ResourceUpdateSystem(client))
                .build());
        net.lapidist.colony.client.entities.PlayerFactory.create(
                world,
                null,
                new net.lapidist.colony.components.state.ResourceData(),
                null
        );
        world.process();

        client.injectResourceUpdate(new ResourceUpdateData(0, 0, java.util.Map.of(
                "WOOD", UPDATED_WOOD,
                "STONE", 0,
                "FOOD", 0
        )));
        world.process();
        world.process();

        com.artemis.utils.IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        Entity mapEntity = world.getEntity(maps.get(0));
        Entity tileEntity = world.getMapper(MapComponent.class)
                .get(mapEntity)
                .getTiles()
                .get(0);
        ResourceComponent rc = world.getMapper(ResourceComponent.class).get(tileEntity);
        assertEquals(UPDATED_WOOD, rc.getWood());

        com.artemis.utils.IntBag players = world.getAspectSubscriptionManager()
                .get(Aspect.all(PlayerResourceComponent.class))
                .getEntities();
        Entity player = world.getEntity(players.get(0));
        PlayerResourceComponent pr = world.getMapper(PlayerResourceComponent.class)
                .get(player);
        assertEquals(UPDATED_WOOD, pr.getWood());
    }
}
