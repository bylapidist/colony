package net.lapidist.colony.client.render;

import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.client.render.data.RenderTile;

/**
 * Read-only data describing map entities for rendering.
 */
public interface MapRenderData {
    Array<RenderTile> getTiles();

    Array<RenderBuilding> getBuildings();

    RenderTile getTile(int x, int y);

    RenderBuilding getBuilding(int x, int y);
}
