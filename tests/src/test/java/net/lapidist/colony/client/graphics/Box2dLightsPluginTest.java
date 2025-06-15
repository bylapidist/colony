package net.lapidist.colony.client.graphics;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/** Basic checks for {@link Box2dLightsPlugin}. */
@RunWith(GdxTestRunner.class)
public class Box2dLightsPluginTest {

    @Test
    public void createInitializesHandler() {
        Box2dLightsPlugin plugin = new Box2dLightsPlugin();
        assertNull(plugin.getRayHandler());

        assertNull(plugin.create(new ShaderManager()));

        assertNull(plugin.getRayHandler());
        assertEquals("box2dlights", plugin.id());
        assertEquals("Box2DLights", plugin.displayName());
    }
}
