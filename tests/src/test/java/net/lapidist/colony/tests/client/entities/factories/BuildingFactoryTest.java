package net.lapidist.colony.tests.client.entities.factories;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.entities.factories.BuildingFactory;
import net.lapidist.colony.components.entities.BuildingComponent;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingFactoryTest {
    private static final int X = 1;
    private static final int Y = 2;

    @Test
    public void createSetsBuildingType() {
        World world = new World(new WorldConfigurationBuilder().build());
        var entity = BuildingFactory.create(world, "HOUSE", new Vector2(X, Y));
        BuildingComponent comp = entity.getComponent(BuildingComponent.class);

        assertEquals("house", comp.getBuildingType());
        assertEquals(X, comp.getX());
        assertEquals(Y, comp.getY());
        world.dispose();
    }
}
