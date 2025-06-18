package net.lapidist.colony.client.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.components.state.MutableEnvironmentState;
import com.badlogic.gdx.math.Vector3;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import box2dLight.RayHandler;
import static org.mockito.Mockito.*;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/** Basic checks for {@link LightsNormalMapShaderPlugin}. */
@RunWith(GdxTestRunner.class)
public class LightsNormalMapShaderPluginTest {

    @Test
    public void createInitializesShaderAndLights() {
        LightsNormalMapShaderPlugin plugin = new LightsNormalMapShaderPlugin();
        assertNull(plugin.getRayHandler());

        ShaderProgram program = plugin.create(new ShaderManager());
        if (program != null) {
            assertNotNull(plugin.getRayHandler());
            program.dispose();
        } else {
            assertNull(plugin.getRayHandler());
        }
        assertEquals("lights-normalmap", plugin.id());
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void updatesLightDirectionWhenTimeChanges() {
        LightsNormalMapShaderPlugin plugin = new LightsNormalMapShaderPlugin();
        MutableEnvironmentState env = new MutableEnvironmentState();
        LightingSystem system = new LightingSystem(
                new ClearScreenSystem(new Color()),
                env
        );
        plugin.setLightingSystem(system);
        ShaderProgram shader = mock(ShaderProgram.class);

        env.setTimeOfDay(0f);
        plugin.applyUniforms(shader);
        ArgumentCaptor<Vector3> firstCap = ArgumentCaptor.forClass(Vector3.class);
        verify(shader).setUniformf(eq("u_lightDir"), firstCap.capture());
        Vector3 first = new Vector3(firstCap.getValue());
        reset(shader);

        final float newTime = 6f;
        env.setTimeOfDay(newTime);
        plugin.applyUniforms(shader);
        ArgumentCaptor<Vector3> secondCap = ArgumentCaptor.forClass(Vector3.class);
        verify(shader).setUniformf(eq("u_lightDir"), secondCap.capture());
        Vector3 second = new Vector3(secondCap.getValue());

        assertNotEquals(first.z, second.z);
    }

    @Test
    public void disposeCleansUpResources() throws Exception {
        LightsNormalMapShaderPlugin plugin = new LightsNormalMapShaderPlugin();

        java.lang.reflect.Field worldField = LightsNormalMapShaderPlugin.class.getDeclaredField("world");
        worldField.setAccessible(true);
        java.lang.reflect.Field handlerField = LightsNormalMapShaderPlugin.class.getDeclaredField("rayHandler");
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

    @Test
    public void failedCreationCleansUpWorld() throws Exception {
        LightsNormalMapShaderPlugin plugin = new LightsNormalMapShaderPlugin();

        java.lang.reflect.Field worldField = LightsNormalMapShaderPlugin.class.getDeclaredField("world");
        worldField.setAccessible(true);
        java.lang.reflect.Field handlerField = LightsNormalMapShaderPlugin.class.getDeclaredField("rayHandler");
        handlerField.setAccessible(true);

        ShaderProgram program = mock(ShaderProgram.class);
        when(program.isCompiled()).thenReturn(false);
        when(program.getLog()).thenReturn("err");

        ShaderManager manager = new ShaderManager((v, f) -> program);

        assertNull(plugin.create(manager));

        assertNull(worldField.get(plugin));
        assertNull(handlerField.get(plugin));
    }
}
