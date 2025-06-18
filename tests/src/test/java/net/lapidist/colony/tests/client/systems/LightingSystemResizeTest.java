package net.lapidist.colony.tests.client.systems;

import box2dLight.RayHandler;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.events.ResizeEvent;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.tests.GdxTestRunner;
import net.mostlyoriginal.api.event.common.EventSystem;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

/** Verifies viewport updates on resize events. */
@RunWith(GdxTestRunner.class)
@SuppressWarnings("checkstyle:magicnumber")
public class LightingSystemResizeTest {

    @Test
    public void updatesHandlerViewportOnResize() {
        ClearScreenSystem clear = new ClearScreenSystem(new Color());
        LightingSystem lighting = new LightingSystem(clear);
        RayHandler handler = mock(RayHandler.class);
        lighting.setRayHandler(handler);

        World world = new World(new WorldConfigurationBuilder()
                .with(new EventSystem(), clear, lighting)
                .build());
        Events.init(world.getSystem(EventSystem.class));

        final int width = 800;
        final int height = 600;
        Events.dispatch(new ResizeEvent(width, height));
        Events.update();

        verify(handler).useCustomViewport(0, 0, width, height);
        world.dispose();
    }
}
