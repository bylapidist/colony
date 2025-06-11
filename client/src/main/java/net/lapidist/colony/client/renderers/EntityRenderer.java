package net.lapidist.colony.client.renderers;

import net.lapidist.colony.client.render.MapRenderData;

/**
 * Common interface for entity renderers.
 */
public interface EntityRenderer<T> {
    /**
     * Render game entities contained in the provided {@link MapRenderData}.
     *
     * @param map render data snapshot
     */
    void render(MapRenderData map);
}
