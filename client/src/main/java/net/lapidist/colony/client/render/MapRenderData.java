package net.lapidist.colony.client.render;

import com.badlogic.gdx.utils.Array;

/**
 * Read-only data describing map entities for rendering.
 */
public interface MapRenderData {
    Array<RenderTile> getTiles();

    Array<RenderBuilding> getBuildings();

    /** Returns the tile at the given map coordinates or {@code null} if none exists. */
    RenderTile getTile(int x, int y);

    /** Returns the map version used to generate this data. */
    int getVersion();
}
