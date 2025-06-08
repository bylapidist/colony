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
import net.lapidist.colony.map.DefaultMapGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapInitSystemTest {

    @Test
    public void loadsProvidedStateIntoWorld() {
        MapState state = new MapState();
        TileData tile = new TileData();
        tile.setX(0);
        tile.setY(0);
        tile.setTileType("GRASS");
        tile.setTextureRef("grass0");
        tile.setPassable(true);
        state.tiles().add(tile);

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapInitSystem(new ProvidedMapStateProvider(state)))
                .build());
        world.process();

        IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        assertEquals(1, maps.size());
    }

    @Test
    public void generatesStateWithGenerator() {
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapInitSystem(new GeneratedMapStateProvider(new DefaultMapGenerator(), 1, 1)))
                .build());
        world.process();

        IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        assertEquals(1, maps.size());
    }
}
