package net.lapidist.colony.tests.client.entities.factories;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.entities.factories.TileFactory;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.resources.ResourceComponent;
import net.lapidist.colony.components.state.ResourceData;
import org.junit.Test;

import static org.junit.Assert.*;

public class TileFactoryTest {
    private static final int X = 3;
    private static final int Y = 4;
    private static final int WOOD = 5;
    private static final int STONE = 6;
    private static final int FOOD = 7;

    @Test
    public void createSetsTileProperties() {
        World world = new World(new WorldConfigurationBuilder().build());
        var entity = TileFactory.create(
                world,
                "GRASS",
                new Vector2(X, Y),
                true,
                false,
                null
        );
        TileComponent tc = entity.getComponent(TileComponent.class);

        assertEquals("grass", tc.getTileType());
        assertTrue(tc.isPassable());
        assertFalse(tc.isSelected());
        assertEquals(X, tc.getX());
        assertEquals(Y, tc.getY());
        world.dispose();
    }

    @Test
    public void createInitializesResourcesFromData() {
        World world = new World(new WorldConfigurationBuilder().build());
        ResourceData data = new ResourceData(WOOD, STONE, FOOD);
        var entity = TileFactory.create(
                world,
                "GRASS",
                new Vector2(X, Y),
                true,
                false,
                data
        );
        ResourceComponent rc = entity.getComponent(ResourceComponent.class);

        assertEquals(WOOD, rc.getWood());
        assertEquals(STONE, rc.getStone());
        assertEquals(FOOD, rc.getFood());
        world.dispose();
    }
}
