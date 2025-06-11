package net.lapidist.colony.tests.components;

import com.badlogic.gdx.utils.Array;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.TilePos;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapComponentTest {
    @Test
    public void versionIncrementsOnModification() {
        MapComponent map = new MapComponent();
        map.setTiles(new Array<>());
        map.setEntities(new Array<>());
        map.setTileMap(new java.util.HashMap<>());
        int start = map.getVersion();
        map.incrementVersion();
        assertEquals(start + 1, map.getVersion());
    }

    @Test
    public void addAndRemoveEntitiesIncrementVersion() {
        MapComponent map = new MapComponent();
        map.setTiles(new Array<>());
        map.setEntities(new Array<>());
        map.setTileMap(new java.util.HashMap<>());
        World world = new World(new WorldConfigurationBuilder().build());
        Entity e = world.createEntity();
        int start = map.getVersion();
        map.addEntity(e);
        assertEquals(start + 1, map.getVersion());
        map.removeEntity(e);
        assertEquals(start + 2, map.getVersion());
    }

    @Test
    public void resetClearsAndIncrements() {
        World world = new World(new WorldConfigurationBuilder().build());
        MapComponent map = new MapComponent();
        map.setTiles(new Array<>(new Entity[]{world.createEntity()}));
        map.setEntities(new Array<>(new Entity[]{world.createEntity()}));
        map.setTileMap(new java.util.HashMap<>(java.util.Map.of(new TilePos(0, 0), world.createEntity())));
        map.reset();
        assertEquals(1, map.getVersion());
        assertEquals(0, map.getTiles().size);
        assertEquals(0, map.getEntities().size);
        assertTrue(map.getTileMap().isEmpty());
    }
}
