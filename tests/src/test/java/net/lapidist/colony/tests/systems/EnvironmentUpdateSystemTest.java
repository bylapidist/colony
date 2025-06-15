package net.lapidist.colony.tests.systems;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.network.EnvironmentUpdateSystem;
import net.lapidist.colony.client.systems.network.MapLoadSystem;
import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.EnvironmentUpdate;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.Season;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/** Tests for {@link EnvironmentUpdateSystem}. */
@RunWith(GdxTestRunner.class)
public class EnvironmentUpdateSystemTest {
    private static final float NEW_TIME = 12f;
    @Test
    public void appliesEnvironmentUpdates() {
        MapState state = new MapState();
        GameClient client = new GameClient();
        client.setMapState(state);
        World world = new World(new WorldConfigurationBuilder()
                .with(new MapLoadSystem(state), new EnvironmentUpdateSystem(client))
                .build());
        world.process();

        client.injectEnvironmentUpdate(new EnvironmentUpdate(new EnvironmentState(NEW_TIME, Season.SUMMER, 0f)));
        world.process();

        assertEquals(NEW_TIME, client.getMapState().environment().timeOfDay(), 0f);
        assertEquals(Season.SUMMER, client.getMapState().environment().season());
        world.dispose();
    }
}
