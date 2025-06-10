package net.lapidist.colony.client.entities.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.entities.BuildingComponent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BuildingFactoryTest {

    @Test
    public void setsBuildingComponentType() {
        World world = new World();
        Entity entity = BuildingFactory.create(
                world,
                BuildingComponent.BuildingType.MARKET,
                "market0",
                new Vector2(0, 0)
        );

        BuildingComponent component = world.getMapper(BuildingComponent.class).get(entity);
        assertEquals(BuildingComponent.BuildingType.MARKET, component.getBuildingType());
    }
}
