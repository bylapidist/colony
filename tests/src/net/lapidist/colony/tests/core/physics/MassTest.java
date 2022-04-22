package net.lapidist.colony.tests.core.physics;

import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.core.physics.Mass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MassTest {

    @Test
    public final void testApplyForce() {
        final float testForce = 10f;

        Vector2 startPosition = new Vector2(0, 0);
        Vector2 endPosition = new Vector2(testForce, testForce);

        Vector2 output = Mass.applyForce(
                startPosition,
                new Vector2(0, 0),
                new Vector2(testForce, testForce),
                1
        );

        assertEquals(output, endPosition);
    }

    @Test
    public final void testApplyForceWithStartingConditions() {
        final float testStartingX = 10f;
        final float testStartingY = 200f;
        final float testAccelerationX = 1000f;
        final float testAccelerationY = 200f;
        final float testForce = 10f;
        final float testMass = 100f;
        final float expectedX = 1010.1f;
        final float expectedY = 400.1f;

        Vector2 startPosition = new Vector2(testStartingX, testStartingY);
        Vector2 endPosition = new Vector2(expectedX, expectedY);

        Vector2 output = Mass.applyForce(
                startPosition,
                new Vector2(testAccelerationX, testAccelerationY),
                new Vector2(testForce, testForce),
                testMass
        );

        assertEquals(output, endPosition);
    }
}
