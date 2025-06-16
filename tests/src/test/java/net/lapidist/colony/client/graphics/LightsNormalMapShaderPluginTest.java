package net.lapidist.colony.client.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.client.systems.DayNightSystem;
import net.lapidist.colony.client.systems.LightingSystem;
import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.Season;
import com.badlogic.gdx.math.Vector3;
import org.mockito.ArgumentCaptor;
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
        DayNightSystem system = new DayNightSystem(
                new ClearScreenSystem(new Color()),
                new LightingSystem(),
                new EnvironmentState(0f, Season.SPRING, 0f)
        );
        plugin.setDayNightSystem(system);
        ShaderProgram shader = mock(ShaderProgram.class);

        system.setTimeOfDay(0f);
        plugin.applyUniforms(shader);
        ArgumentCaptor<Vector3> firstCap = ArgumentCaptor.forClass(Vector3.class);
        verify(shader).setUniformf(eq("u_lightDir"), firstCap.capture());
        Vector3 first = new Vector3(firstCap.getValue());
        reset(shader);

        final float newTime = 6f;
        system.setTimeOfDay(newTime);
        plugin.applyUniforms(shader);
        ArgumentCaptor<Vector3> secondCap = ArgumentCaptor.forClass(Vector3.class);
        verify(shader).setUniformf(eq("u_lightDir"), secondCap.capture());
        Vector3 second = new Vector3(secondCap.getValue());

        assertNotEquals(first.x, second.x);
    }
}
