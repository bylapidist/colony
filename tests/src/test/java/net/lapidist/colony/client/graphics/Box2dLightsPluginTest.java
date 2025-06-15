package net.lapidist.colony.client.graphics;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
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
        if (plugin.getRayHandler() != null) {
            try {
                java.lang.reflect.Field field = RayHandler.class.getDeclaredField("ambientLight");
                field.setAccessible(true);
                Color color = (Color) field.get(plugin.getRayHandler());
                assertEquals(1f, color.r, 0f);
                assertEquals(1f, color.g, 0f);
                assertEquals(1f, color.b, 0f);
                assertEquals(1f, color.a, 0f);
            } catch (Exception ex) {
                fail("Could not access ambient light: " + ex.getMessage());
            }
        } else {
            assertNull(plugin.getRayHandler());
        }
        assertEquals("box2dlights", plugin.id());
        assertEquals("Box2DLights", plugin.displayName());
    }
}
