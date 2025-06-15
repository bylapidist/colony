package net.lapidist.colony.client.renderers;

import com.artemis.World;

/**
 * Factory interface for creating {@link MapRenderer} instances.
 */
public interface MapRendererFactory {
    /**
     * Create a renderer for the given world.
     *
     * @param world game world context
     * @return configured map renderer
     */
    MapRenderer create(World world, net.lapidist.colony.client.graphics.ShaderPlugin plugin);
}
