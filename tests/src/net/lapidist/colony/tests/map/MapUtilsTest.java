package net.lapidist.colony.tests.map;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.map.MapUtils;
import net.lapidist.colony.components.maps.TileComponent;
import com.artemis.ComponentMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapUtilsTest {

    @Test
    public void returnsEmptyWhenNoMapPresent() {
        World world = new World(new WorldConfigurationBuilder().build());
        assertTrue(MapUtils.findMapEntity(world).isEmpty());
        assertTrue(MapUtils.findMap(world).isEmpty());
    }

    @Test
    public void findsMapEntityAndComponent() {
        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .textureRef("grass0")
                .passable(true)
                .build();
        state.tiles().put(new TilePos(0, 0), tile);

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state))
                .build());
        world.process();

        assertTrue(MapUtils.findMapEntity(world).isPresent());
        MapComponent map = MapUtils.findMap(world).orElse(null);
        assertNotNull(map);
        assertEquals(1, map.getTiles().size);
        assertTrue(map.getTileMap().containsKey(new TilePos(0, 0)));
    }

    @Test
    public void findsTileByCoordinates() {
        MapState state = new MapState();
        TileData tile = TileData.builder()
                .x(1)
                .y(2)
                .tileType("GRASS")
                .textureRef("grass0")
                .passable(true)
                .build();
        state.tiles().put(new TilePos(1, 2), tile);

        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state))
                .build());
        world.process();

        MapComponent map = MapUtils.findMap(world).orElse(null);
        assertNotNull(map);
        ComponentMapper<TileComponent> mapper = world.getMapper(TileComponent.class);

        assertTrue(MapUtils.findTile(map, 1, 2, mapper).isPresent());
        assertTrue(MapUtils.findTile(map, 0, 0, mapper).isEmpty());
    }
}
