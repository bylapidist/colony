package net.lapidist.colony.client.render;

import com.badlogic.gdx.utils.Array;

/**
 * Basic immutable implementation of {@link MapRenderData}.
 */
public final class SimpleMapRenderData implements MapRenderData {
    private final Array<RenderTile> tiles;
    private final Array<RenderBuilding> buildings;
    private final RenderTile[][] tileGrid;
    private int version;

    public SimpleMapRenderData(
            final Array<RenderTile> tilesToUse,
            final Array<RenderBuilding> buildingsToUse,
            final RenderTile[][] grid
    ) {
        this.tiles = tilesToUse;
        this.buildings = buildingsToUse;
        this.tileGrid = grid;
        this.version = 0;
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

    @Override
    public int getVersion() {
        return version;
    }

    /** Sets the map version for this render data. */
    public void setVersion(final int newVersion) {
        this.version = newVersion;
    }

    /**
     * Update the tile at the given index and adjust the grid reference.
     */
    public void updateTile(final int index, final RenderTile tile) {
        RenderTile old = tiles.get(index);
        if (old != null
                && old.getX() >= 0 && old.getY() >= 0
                && old.getX() < tileGrid.length && old.getY() < tileGrid[0].length) {
            tileGrid[old.getX()][old.getY()] = null;
        }
        tiles.set(index, tile);
        if (tile.getX() >= 0 && tile.getY() >= 0
                && tile.getX() < tileGrid.length && tile.getY() < tileGrid[0].length) {
            tileGrid[tile.getX()][tile.getY()] = tile;
        }
    }
}
