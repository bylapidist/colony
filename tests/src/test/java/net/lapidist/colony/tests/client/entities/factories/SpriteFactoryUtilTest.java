package net.lapidist.colony.tests.client.entities.factories;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.client.entities.factories.SpriteFactoryUtil;
import net.lapidist.colony.components.GameConstants;
import org.junit.Test;

import static org.junit.Assert.*;

public class SpriteFactoryUtilTest {
    private static final int X = 5;
    private static final int Y = 7;

    @Test
    public void createEntitySetsComponentBounds() {
        World world = new World(new WorldConfigurationBuilder().build());
        BuildingComponent component = new BuildingComponent();
        Vector2 coords = new Vector2(X, Y);

        SpriteFactoryUtil.createEntity(world, component, coords);

        assertEquals(GameConstants.TILE_SIZE, component.getWidth());
        assertEquals(GameConstants.TILE_SIZE, component.getHeight());
        assertEquals(X, component.getX());
        assertEquals(Y, component.getY());
        world.dispose();
    }
}
