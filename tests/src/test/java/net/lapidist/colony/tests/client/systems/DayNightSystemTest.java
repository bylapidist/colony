package net.lapidist.colony.tests.client.systems;

import box2dLight.RayHandler;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.client.systems.DayNightSystem;
import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.Season;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/** Tests for {@link DayNightSystem}. */
@RunWith(GdxTestRunner.class)
@SuppressWarnings("checkstyle:magicnumber")
public class DayNightSystemTest {

    private static final float NIGHT_RED = 0.05f;
    private static final float NIGHT_BLUE = 0.1f;
    private static final float MOON_RED = 0.45f;
    private static final float TOLERANCE = 0.01f;
    private static final float NOON = 12f;
    private static final float WRAP_VALUE = 25f;

    @Test
    public void updatesLightAndColor() {
        ClearScreenSystem clear = new ClearScreenSystem(new Color());
        LightingSystem lighting = new LightingSystem();
        RayHandler handler = mock(RayHandler.class);
        lighting.setRayHandler(handler);
        java.util.concurrent.atomic.AtomicReference<EnvironmentState> ref =
                new java.util.concurrent.atomic.AtomicReference<>(new EnvironmentState(0f, Season.SPRING, 0f));
        DayNightSystem system = new DayNightSystem(clear, lighting, ref::get);
        ref.set(new EnvironmentState(NOON, Season.SPRING, 0f));
        World world = new World(new WorldConfigurationBuilder()
                .with(clear, lighting, system)
                .build());
        world.setDelta(0f);
        world.process();
        verify(handler).setAmbientLight(1f, 1f, 1f, 1f);
        assertEquals(1f, clear.getColor().r, TOLERANCE);
        ref.set(new EnvironmentState(0f, Season.SPRING, 0f));
        world.process();
//CHECKSTYLE:OFF
        verify(handler).setAmbientLight(NIGHT_RED, NIGHT_RED, NIGHT_BLUE, 1f);
        assertEquals(NIGHT_RED, clear.getColor().r, TOLERANCE);
//CHECKSTYLE:ON
        world.dispose();
    }

    @Test
    public void wrapsTimeOfDay() {
        ClearScreenSystem clear = new ClearScreenSystem(new Color());
        LightingSystem lighting = new LightingSystem();
        java.util.concurrent.atomic.AtomicReference<EnvironmentState> ref =
                new java.util.concurrent.atomic.AtomicReference<>(new EnvironmentState(WRAP_VALUE, Season.SPRING, 0f));
        DayNightSystem system = new DayNightSystem(clear, lighting, ref::get);
        World world = new World(new WorldConfigurationBuilder()
                .with(clear, lighting, system)
                .build());
        world.setDelta(0f);
        world.process();
        final float small = 0.001f;
        assertEquals(1f, system.getTimeOfDay(), small);
        world.dispose();
    }

    @Test
    public void appliesMoonlight() {
        ClearScreenSystem clear = new ClearScreenSystem(new Color());
        LightingSystem lighting = new LightingSystem();
        RayHandler handler = mock(RayHandler.class);
        lighting.setRayHandler(handler);
        java.util.concurrent.atomic.AtomicReference<EnvironmentState> ref =
                new java.util.concurrent.atomic.AtomicReference<>(new EnvironmentState(0f, Season.SPRING, 1f));
        DayNightSystem system = new DayNightSystem(clear, lighting, ref::get);
        World world = new World(new WorldConfigurationBuilder()
                .with(clear, lighting, system)
                .build());
        world.setDelta(0f);
        world.process();
//CHECKSTYLE:OFF
        verify(handler).setAmbientLight(anyFloat(), anyFloat(), anyFloat(), eq(1f));
        assertEquals(MOON_RED, clear.getColor().r, TOLERANCE);
//CHECKSTYLE:ON
        world.dispose();
    }
}
