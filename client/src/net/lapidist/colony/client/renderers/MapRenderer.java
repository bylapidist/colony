package net.lapidist.colony.client.renderers;

import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.components.maps.MapComponent;

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
    void render(MapComponent map, PlayerCameraSystem camera);
}
