package net.lapidist.colony.tests.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.IntBag;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.client.systems.network.TileUpdateSystem;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class TileUpdateSystemTest {

    @Test
    public void appliesServerTileSelection() {
        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .textureRef("grass0")
                .passable(true)
                .build();
        state.tiles().put(new TilePos(0, 0), tile);

        GameClient client = new GameClient();
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new TileUpdateSystem(client))
                .build());

        world.process();

        TileSelectionData data = new TileSelectionData(0, 0, true);
        client.injectTileSelectionUpdate(data);

        world.process();

        IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        Entity map = world.getEntity(maps.get(0));
        MapComponent mapComponent = world.getMapper(MapComponent.class).get(map);
        TileComponent tileComponent = world.getMapper(TileComponent.class)
                .get(mapComponent.getTiles().get(0));

        assertTrue(tileComponent.isSelected());
    }
}
