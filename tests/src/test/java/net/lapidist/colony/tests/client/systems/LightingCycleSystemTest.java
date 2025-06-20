package net.lapidist.colony.tests.client.systems;

import box2dLight.RayHandler;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.tests.GdxTestRunner;
import net.lapidist.colony.components.state.MutableEnvironmentState;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/** Tests day/night behavior within {@link LightingSystem}. */
@RunWith(GdxTestRunner.class)
@SuppressWarnings("checkstyle:magicnumber")
public class LightingCycleSystemTest {

    private static final float DAY_RED = 0.6f;
    private static final float NIGHT_RED = 0f;
    private static final float NIGHT_AMBIENT = 0.1f;
    private static final float TOLERANCE = 0.01f;

    @Test
    public void updatesLightAndColor() {
        ClearScreenSystem clear = new ClearScreenSystem(new Color());
        MutableEnvironmentState env = new MutableEnvironmentState();
        LightingSystem lighting = new LightingSystem(clear, env);
        RayHandler handler = mock(RayHandler.class);
        lighting.setRayHandler(handler);
        final float noon = 12f;
        env.setTimeOfDay(noon);
        World world = new World(new WorldConfigurationBuilder()
                .with(clear, lighting)
                .build());
        world.setDelta(0f);
        world.process();
        verify(handler).setAmbientLight(0f, 0f, 0f, 1f);
        assertEquals(DAY_RED, clear.getColor().r, TOLERANCE);
        env.setTimeOfDay(0f);
        world.process();
        verify(handler).setAmbientLight(NIGHT_AMBIENT, NIGHT_AMBIENT, NIGHT_AMBIENT, 1f);
        assertEquals(NIGHT_RED, clear.getColor().r, TOLERANCE);
        world.dispose();
    }

    @Test
    public void wrapsTimeOfDay() {
        ClearScreenSystem clear = new ClearScreenSystem(new Color());
        MutableEnvironmentState env = new MutableEnvironmentState();
        LightingSystem lighting = new LightingSystem(clear, env);
        final float wrapValue = 25f;
        env.setTimeOfDay(wrapValue);
        World world = new World(new WorldConfigurationBuilder()
                .with(clear, lighting)
                .build());
        world.setDelta(0f);
        world.process();
        final float small = 0.001f;
        assertEquals(1f, lighting.getTimeOfDay(), small);
        world.dispose();
    }
}
