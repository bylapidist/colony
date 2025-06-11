package net.lapidist.colony.render;

import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.render.data.RenderBuilding;
import net.lapidist.colony.render.data.RenderTile;

/**
 * Read-only data describing map entities for rendering.
 */
public interface MapRenderData {
    Array<RenderTile> getTiles();

    Array<RenderBuilding> getBuildings();
}
