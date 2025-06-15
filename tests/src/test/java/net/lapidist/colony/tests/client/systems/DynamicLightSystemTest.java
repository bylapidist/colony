package net.lapidist.colony.tests.client.systems;

import box2dLight.RayHandler;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import static org.mockito.Mockito.*;
import net.lapidist.colony.client.systems.DynamicLightSystem;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.components.light.PointLightComponent;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/** Tests for {@link DynamicLightSystem}. */
@RunWith(GdxTestRunner.class)
public class DynamicLightSystemTest {

    private static final float RADIUS = 5f;
    private static final float INTENSITY = 0.5f;

    @Test
    public void createsAndRemovesLights() {
        LightingSystem lighting = new LightingSystem();
        DynamicLightSystem.LightFactory factory = (h, c) -> mock(box2dLight.PointLight.class);
        DynamicLightSystem system = new DynamicLightSystem(lighting, factory);
        World world = new World(new WorldConfigurationBuilder()
                .with(lighting, system)
                .build());
        RayHandler handler = mock(RayHandler.class);
        lighting.setRayHandler(handler);

        var e = world.createEntity();
        PointLightComponent light = new PointLightComponent();
        light.setColor(new Color(1f, 0f, 0f, 1f));
        light.setRadius(RADIUS);
        light.setIntensity(INTENSITY);
        PlayerComponent pc = new PlayerComponent();
        pc.setX(1f);
        pc.setY(2f);
        e.edit().add(light).add(pc);

        world.setDelta(0f);
        world.process();
        assertEquals(1, system.getLightCount());

        e.deleteFromWorld();
        world.process();
        assertEquals(0, system.getLightCount());

        world.dispose();
        handler.dispose();
    }
}
