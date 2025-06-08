package net.lapidist.colony.tests.map;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.map.MapUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapUtilsTest {

    @Test
    public void returnsNullWhenNoMapPresent() {
        World world = new World(new WorldConfigurationBuilder().build());
        assertNull(MapUtils.findMapEntity(world));
        assertNull(MapUtils.findMap(world));
    }

    @Test
    public void findsMapEntityAndComponent() {
        MapState state = new MapState();
        TileData tile = new TileData();
        tile.setX(0);
        tile.setY(0);
        tile.setTileType("GRASS");
        tile.setTextureRef("grass0");
        tile.setPassable(true);
        state.tiles().add(tile);

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state))
                .build());
        world.process();

        assertNotNull(MapUtils.findMapEntity(world));
        MapComponent map = MapUtils.findMap(world);
        assertNotNull(map);
        assertEquals(1, map.getTiles().size);
    }
}
