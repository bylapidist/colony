package net.lapidist.colony.client.renderers;

import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.MapRenderData;

/**
 * Rendering abstraction for map visuals.
 */
public interface MapRenderer {
    /**
     * Render the supplied map using the given camera system.
     *
     * @param map map data to render
     * @param camera player camera system controlling the viewport
     */
    void render(MapRenderData map, CameraProvider camera);
}
