package net.lapidist.colony.client.render;

import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.client.render.data.RenderTile;

/**
 * Basic immutable implementation of {@link MapRenderData}.
 */
public final class SimpleMapRenderData implements MapRenderData {
    private final Array<RenderTile> tiles;
    private final Array<RenderBuilding> buildings;

    public SimpleMapRenderData(final Array<RenderTile> tilesToUse, final Array<RenderBuilding> buildingsToUse) {
        this.tiles = tilesToUse;
        this.buildings = buildingsToUse;
    }

    @Override
    public Array<RenderTile> getTiles() {
        return tiles;
    }

    @Override
    public Array<RenderBuilding> getBuildings() {
        return buildings;
    }
}
