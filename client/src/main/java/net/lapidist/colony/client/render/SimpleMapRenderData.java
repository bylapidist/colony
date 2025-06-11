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
    private final RenderTile[][] tileGrid;

    public SimpleMapRenderData(
            final Array<RenderTile> tilesToUse,
            final Array<RenderBuilding> buildingsToUse,
            final RenderTile[][] grid
    ) {
        this.tiles = tilesToUse;
        this.buildings = buildingsToUse;
        this.tileGrid = grid;
    }

    @Override
    public Array<RenderTile> getTiles() {
        return tiles;
    }

    @Override
    public Array<RenderBuilding> getBuildings() {
        return buildings;
    }

    @Override
    public RenderTile getTile(final int x, final int y) {
        if (x < 0 || y < 0 || x >= tileGrid.length || y >= tileGrid[0].length) {
            return null;
        }
        return tileGrid[x][y];
    }
}
