package net.lapidist.colony.client.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Discovers available {@link ShaderPlugin} implementations.
 */
public final class ShaderPluginLoader {

    /**
     * Load all shader plugins using {@link ServiceLoader}.
     *
     * @return list of discovered plugins
     */
    public List<ShaderPlugin> loadPlugins() {
        List<ShaderPlugin> plugins = new ArrayList<>();
        for (ShaderPlugin plugin : ServiceLoader.load(ShaderPlugin.class)) {
            plugins.add(plugin);
        }
        return plugins;
    }
}
