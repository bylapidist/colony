package net.lapidist.colony.tests.systems;

import com.artemis.Aspect;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.entities.PlayerFactory;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.components.light.PointLightComponent;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/** Tests for {@link PlayerFactory}. */
@RunWith(GdxTestRunner.class)
public class PlayerFactoryTest {

    private static final float EXPECTED_RADIUS = 3f;
    private static final float EXPECTED_INTENSITY = 0.6f;
    private static final com.badlogic.gdx.graphics.Color EXPECTED_COLOR =
            new com.badlogic.gdx.graphics.Color(1f, 0.6f, 0.2f, 1f);

    @Test
    public void addsLightToPlayer() {
        World world = new World(new WorldConfigurationBuilder().build());
        PlayerFactory.create(
                world,
                null,
                new net.lapidist.colony.components.state.ResourceData(),
                null
        );
        world.process();
        var ids = world.getAspectSubscriptionManager()
                .get(Aspect.all(PlayerComponent.class))
                .getEntities();
        assertEquals(1, ids.size());
        var light = world.getMapper(PointLightComponent.class)
                .get(world.getEntity(ids.get(0)));
        assertNotNull(light);
        assertEquals(EXPECTED_RADIUS, light.getRadius(), 0f);
        assertEquals(EXPECTED_INTENSITY, light.getIntensity(), 0f);
        assertEquals(EXPECTED_COLOR.r, light.getColor().r, 0f);
        assertEquals(EXPECTED_COLOR.g, light.getColor().g, 0f);
        assertEquals(EXPECTED_COLOR.b, light.getColor().b, 0f);
    }
}
