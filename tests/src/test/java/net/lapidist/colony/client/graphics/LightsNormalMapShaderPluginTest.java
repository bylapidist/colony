package net.lapidist.colony.client.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
}
