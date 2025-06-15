package net.lapidist.colony.client.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.client.systems.DayNightSystem;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.components.state.EnvironmentState;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/** Basic checks for {@link LightsNormalMapShaderPlugin}. */
@RunWith(GdxTestRunner.class)
public class LightsNormalMapShaderPluginTest {

    private static final float START_TIME = 6f;
    private static final float END_TIME = 12f;
    private static final float EPSILON = 0.001f;

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
    public void updatesLightDirectionWhenTimeChanges() {
        LightsNormalMapShaderPlugin plugin = new LightsNormalMapShaderPlugin();
        DayNightSystem dns = new DayNightSystem(
                new ClearScreenSystem(new Color()),
                new LightingSystem(),
                new EnvironmentState()
        );
        PlayerCameraSystem camera = new PlayerCameraSystem();
        plugin.setDayNightSystem(dns);
        plugin.setCameraProvider(camera);

        ShaderProgram program = mock(ShaderProgram.class);
        ArgumentCaptor<Vector3> captor = ArgumentCaptor.forClass(Vector3.class);
        dns.setTimeOfDay(START_TIME);
        plugin.applyUniforms(program);
        verify(program).setUniformf(eq("u_lightDir"), captor.capture());
        Vector3 first = new Vector3(captor.getValue());
        reset(program);
        dns.setTimeOfDay(END_TIME);
        plugin.applyUniforms(program);
        verify(program).setUniformf(eq("u_lightDir"), captor.capture());
        Vector3 second = captor.getValue();
        assertNotEquals(first.x, second.x, EPSILON);
    }
}
