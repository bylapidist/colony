package net.lapidist.colony.tests.client.systems;

import box2dLight.RayHandler;
import box2dLight.DirectionalLight;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import static org.mockito.Mockito.*;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.components.state.MutableEnvironmentState;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.components.light.PointLightComponent;
import net.lapidist.colony.components.light.DirectionalLightComponent;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/** Tests dynamic light management in {@link LightingSystem}. */
@RunWith(GdxTestRunner.class)
public class LightingSystemDynamicTest {

    private static final float RADIUS = 5f;
    private static final float INTENSITY = 0.5f;
    private static final float SOFTNESS = 4f;

    @Test
    public void createsAndRemovesLights() {
        ClearScreenSystem clear = new ClearScreenSystem(new Color());
        final box2dLight.PointLight created = mock(box2dLight.PointLight.class);
        LightingSystem.LightFactory factory = new LightingSystem.LightFactory() {
            @Override
            public box2dLight.PointLight create(final RayHandler h, final PointLightComponent c) {
                return created;
            }

            @Override
            public DirectionalLight createDirectional(final RayHandler h, final DirectionalLightComponent c) {
                return null;
            }
        };
        MutableEnvironmentState env = new MutableEnvironmentState();
        LightingSystem lighting = new LightingSystem(clear, factory, env);
        lighting.setSoftShadowsEnabled(true);
        lighting.setShadowSoftnessLength(SOFTNESS);
        World world = new World(new WorldConfigurationBuilder()
                .with(clear, lighting)
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
        assertEquals(1, lighting.getLightCount());
        verify(created).setSoft(true);
        verify(created).setSoftnessLength(SOFTNESS);

        e.deleteFromWorld();
        world.process();
        assertEquals(0, lighting.getLightCount());

        world.dispose();
        handler.dispose();
    }

    @Test
    public void usesConfiguredRayCount() {
        final int rays = 24;
        MutableEnvironmentState env2 = new MutableEnvironmentState();
        LightingSystem lighting = new LightingSystem(new ClearScreenSystem(new Color()), rays, env2);
        assertEquals(rays, lighting.getRayCount());
    }
}
