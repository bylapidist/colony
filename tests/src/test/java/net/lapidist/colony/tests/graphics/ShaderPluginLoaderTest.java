package net.lapidist.colony.tests.graphics;

import net.lapidist.colony.client.graphics.ShaderPlugin;
import net.lapidist.colony.client.graphics.ShaderPluginLoader;
import net.lapidist.colony.client.graphics.NullShaderPlugin;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class ShaderPluginLoaderTest {

    @Test
    public void loadsDefaultPlugin() {
        ShaderPluginLoader loader = new ShaderPluginLoader();
        List<ShaderPlugin> plugins = loader.loadPlugins();
        assertFalse(plugins.isEmpty());
        ShaderPlugin plugin = plugins.get(0);
        assertTrue(plugin instanceof NullShaderPlugin);
        assertEquals("none", plugin.id());
        assertEquals("None", plugin.displayName());
        assertNull(plugin.create(new net.lapidist.colony.client.graphics.ShaderManager()));
    }
}
