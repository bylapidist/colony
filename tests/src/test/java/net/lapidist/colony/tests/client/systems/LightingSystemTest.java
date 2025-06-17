package net.lapidist.colony.tests.client.systems;

import box2dLight.RayHandler;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import net.lapidist.colony.tests.GdxTestRunner;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

/** Tests for {@link LightingSystem}. */
@RunWith(GdxTestRunner.class)
public class LightingSystemTest {

    @Test
    public void updatesAndDisposesHandler() {
        RayHandler handler = mock(RayHandler.class);
        ClearScreenSystem clear = new ClearScreenSystem(new com.badlogic.gdx.graphics.Color());
        LightingSystem system = new LightingSystem(clear);
        system.setRayHandler(handler);

        World world = new World(new WorldConfigurationBuilder().with(clear, system).build());
        world.setDelta(0f);
        world.process();

        verify(handler).update();
        system.dispose();
        verify(handler).dispose();
        world.dispose();
    }

    @Test
    public void skipsWhenHandlerMissing() {
        ClearScreenSystem clear = new ClearScreenSystem(new com.badlogic.gdx.graphics.Color());
        LightingSystem system = new LightingSystem(clear);
        World world = new World(new WorldConfigurationBuilder().with(clear, system).build());
        world.setDelta(0f);
        world.process();
        system.dispose();
        world.dispose();
    }
}
