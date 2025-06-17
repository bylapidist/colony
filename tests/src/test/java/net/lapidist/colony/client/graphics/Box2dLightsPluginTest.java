package net.lapidist.colony.client.graphics;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/** Basic checks for {@link Box2dLightsPlugin}. */
@RunWith(GdxTestRunner.class)
public class Box2dLightsPluginTest {

    @Test
    public void createInitializesHandler() {
        Box2dLightsPlugin plugin = new Box2dLightsPlugin();
        assertNull(plugin.getRayHandler());

        ShaderProgram program = plugin.create(new ShaderManager());
        if (program != null) {
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
            program.dispose();
        } else {
            assertNull(plugin.getRayHandler());
        }
        assertEquals("box2dlights", plugin.id());
        assertEquals("Box2DLights", plugin.displayName());
    }

    @Test
    public void disposeCleansUpResources() throws Exception {
        Box2dLightsPlugin plugin = new Box2dLightsPlugin();

        java.lang.reflect.Field worldField = Box2dLightsPlugin.class.getDeclaredField("world");
        worldField.setAccessible(true);
        java.lang.reflect.Field handlerField = Box2dLightsPlugin.class.getDeclaredField("rayHandler");
        handlerField.setAccessible(true);

        com.badlogic.gdx.physics.box2d.World world = mock(com.badlogic.gdx.physics.box2d.World.class);
        RayHandler handler = mock(RayHandler.class);
        worldField.set(plugin, world);
        handlerField.set(plugin, handler);

        plugin.dispose();

        InOrder order = inOrder(world, handler);
        order.verify(world).dispose();
        order.verify(handler).dispose();

        assertNull(worldField.get(plugin));
        assertNull(handlerField.get(plugin));
    }
}
