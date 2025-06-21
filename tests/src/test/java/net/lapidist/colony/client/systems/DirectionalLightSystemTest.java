package net.lapidist.colony.client.systems;

import box2dLight.DirectionalLight;
import box2dLight.RayHandler;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.components.entities.CelestialBodyComponent;
import net.lapidist.colony.components.light.DirectionalLightComponent;
import net.lapidist.colony.components.state.MutableEnvironmentState;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/** Tests directional light management within {@link LightingSystem}. */
@RunWith(GdxTestRunner.class)
public class DirectionalLightSystemTest {

    @Test
    public void createsAndRemovesDirectionalLights() {
        ClearScreenSystem clear = new ClearScreenSystem(new Color());
        LightingSystem.LightFactory factory = new LightingSystem.LightFactory() {
            @Override
            public box2dLight.PointLight create(final RayHandler h, final net.lapidist.colony.components.light.PointLightComponent c) {
                return null;
            }

            @Override
            public DirectionalLight createDirectional(final RayHandler h, final DirectionalLightComponent c) {
                return mock(DirectionalLight.class);
            }
        };
        MutableEnvironmentState env = new MutableEnvironmentState();
        LightingSystem lighting = new LightingSystem(clear, factory, env);
        World world = new World(new WorldConfigurationBuilder().with(clear, lighting).build());
        RayHandler handler = mock(RayHandler.class);
        lighting.setRayHandler(handler);

        var e = world.createEntity();
        DirectionalLightComponent light = new DirectionalLightComponent();
        CelestialBodyComponent body = new CelestialBodyComponent();
        body.setOrbitRadius(0f);
        body.setOrbitOffset(0f);
        e.edit().add(light).add(body);

        world.setDelta(0f);
        world.process();
        assertEquals(1, lighting.getDirectionalLightCount());

        e.deleteFromWorld();
        world.process();
        assertEquals(0, lighting.getDirectionalLightCount());

        world.dispose();
        handler.dispose();
    }
}
