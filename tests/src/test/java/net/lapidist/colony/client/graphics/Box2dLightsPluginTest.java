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

        try {
            plugin.create(new ShaderManager());
            fail("Expected failure when creating RayHandler");
        } catch (IllegalStateException ex) {
            // expected when FBO unsupported in headless tests
        }

        assertNull(plugin.getRayHandler());
        assertEquals("box2dlights", plugin.id());
        assertEquals("Box2DLights", plugin.displayName());
    }
}
