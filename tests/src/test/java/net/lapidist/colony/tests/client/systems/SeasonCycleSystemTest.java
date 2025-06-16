package net.lapidist.colony.tests.client.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.systems.SeasonCycleSystem;
import net.lapidist.colony.components.state.MutableEnvironmentState;
import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.Season;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/** Tests for {@link SeasonCycleSystem}. */
@RunWith(GdxTestRunner.class)
public class SeasonCycleSystemTest {
    @Test
    public void advancesSeasonAfterInterval() {
        MutableEnvironmentState env = new MutableEnvironmentState(new EnvironmentState());
        SeasonCycleSystem system = new SeasonCycleSystem(env, 1f);
        World world = new World(new WorldConfigurationBuilder().with(system).build());
        world.setDelta(1f);
        world.process();
        assertEquals(Season.SUMMER, env.getSeason());
        world.dispose();
    }
}
