package net.lapidist.colony.tests.client.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.components.state.MutableEnvironmentState;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/** Verifies sun direction flips below the horizon at night. */
@RunWith(GdxTestRunner.class)
public class LightingSunDirectionTest {

    private static final float NOON = 12f;

    @Test
    public void midnightBelowHorizon() {
        MutableEnvironmentState env = new MutableEnvironmentState();
        LightingSystem lighting = new LightingSystem(new ClearScreenSystem(new Color()), env);
        env.setTimeOfDay(0f);
        Vector3 dir = lighting.getSunDirection(new Vector3());
        assertTrue(dir.z < 0f);
    }

    @Test
    public void noonAboveHorizon() {
        MutableEnvironmentState env = new MutableEnvironmentState();
        LightingSystem lighting = new LightingSystem(new ClearScreenSystem(new Color()), env);
        env.setTimeOfDay(NOON);
        Vector3 dir = lighting.getSunDirection(new Vector3());
        assertTrue(dir.z > 0f);
    }
}
