package net.lapidist.colony.tests.client.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.systems.CelestialSystem;
import net.lapidist.colony.components.entities.CelestialBodyComponent;
import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.Season;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/** Tests for {@link CelestialSystem}. */
@RunWith(GdxTestRunner.class)
public class CelestialSystemTest {
    private static final float TIME = 6f;
    private static final float EPSILON = 0.01f;
    @Test
    public void updatesBodyPosition() {
        java.util.concurrent.atomic.AtomicReference<EnvironmentState> ref =
                new java.util.concurrent.atomic.AtomicReference<>(new EnvironmentState(TIME, Season.SPRING, 0f));
        CelestialSystem system = new CelestialSystem(null, ref::get);
        World world = new World(new WorldConfigurationBuilder().with(system).build());
        var e = world.createEntity();
        CelestialBodyComponent body = new CelestialBodyComponent();
        body.setTexture("player0");
        body.setOrbitRadius(1f);
        body.setOrbitOffset(0f);
        e.edit().add(body);
        world.setDelta(0f);
        world.process();
        float centerX = MapState.DEFAULT_WIDTH * GameConstants.TILE_SIZE / 2f;
        float centerY = MapState.DEFAULT_HEIGHT * GameConstants.TILE_SIZE / 2f;
        assertEquals(centerX + 1f, body.getX(), EPSILON);
        assertEquals(centerY, body.getY(), EPSILON);
        world.dispose();
    }
}
