package net.lapidist.colony.tests.systems;

import com.artemis.Aspect;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.systems.PlayerInitSystem;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.components.light.PointLightComponent;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/** Tests for {@link PlayerInitSystem}. */
@RunWith(GdxTestRunner.class)
public class PlayerInitSystemTest {

    private static final float EXPECTED_RADIUS = 3f;
    private static final float EXPECTED_INTENSITY = 0.6f;

    @Test
    public void addsLightToPlayer() {
        World world = new World(new WorldConfigurationBuilder()
                .with(new PlayerInitSystem())
                .build());
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
    }
}
