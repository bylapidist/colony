package net.lapidist.colony.tests.client.systems;

import box2dLight.RayHandler;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.client.systems.DayNightSystem;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/** Tests for {@link DayNightSystem}. */
@RunWith(GdxTestRunner.class)
public class DayNightSystemTest {

    @Test
    public void updatesLightAndColor() {
        ClearScreenSystem clear = new ClearScreenSystem(new Color());
        LightingSystem lighting = new LightingSystem();
        RayHandler handler = mock(RayHandler.class);
        lighting.setRayHandler(handler);
        DayNightSystem system = new DayNightSystem(clear, lighting);
        final float noon = 12f;
        system.setTimeOfDay(noon);
        World world = new World(new WorldConfigurationBuilder()
                .with(clear, lighting, system)
                .build());
        world.setDelta(0f);
        world.process();
        verify(handler).setAmbientLight(1f, 1f, 1f, 1f);
        final float tolerance = 0.01f;
        assertEquals(1f, clear.getColor().r, tolerance);
        system.setTimeOfDay(0f);
        world.process();
        verify(handler).setAmbientLight(0f, 0f, 0f, 1f);
        assertEquals(0f, clear.getColor().r, tolerance);
        world.dispose();
    }

    @Test
    public void wrapsTimeOfDay() {
        ClearScreenSystem clear = new ClearScreenSystem(new Color());
        LightingSystem lighting = new LightingSystem();
        DayNightSystem system = new DayNightSystem(clear, lighting);
        final float wrapValue = 25f;
        system.setTimeOfDay(wrapValue);
        World world = new World(new WorldConfigurationBuilder()
                .with(clear, lighting, system)
                .build());
        world.setDelta(0f);
        world.process();
        final float small = 0.001f;
        assertEquals(1f, system.getTimeOfDay(), small);
        world.dispose();
    }
}
