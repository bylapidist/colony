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
    private final java.util.Map<net.lapidist.colony.components.state.TilePos, RenderTile> tileMap;
    private final java.util.Map<net.lapidist.colony.components.state.TilePos, RenderBuilding> buildingMap;

    public SimpleMapRenderData(final Array<RenderTile> tilesToUse, final Array<RenderBuilding> buildingsToUse) {
        this(tilesToUse, buildingsToUse, null, null);
    }

    public SimpleMapRenderData(
            final Array<RenderTile> tilesToUse,
            final Array<RenderBuilding> buildingsToUse,
            final java.util.Map<net.lapidist.colony.components.state.TilePos, RenderTile> tileMapToUse,
            final java.util.Map<net.lapidist.colony.components.state.TilePos, RenderBuilding> buildingMapToUse
    ) {
        this.tiles = tilesToUse;
        this.buildings = buildingsToUse;
        if (tileMapToUse != null) {
            this.tileMap = tileMapToUse;
        } else {
            this.tileMap = buildTileMap(tilesToUse);
        }
        if (buildingMapToUse != null) {
            this.buildingMap = buildingMapToUse;
        } else {
            this.buildingMap = buildBuildingMap(buildingsToUse);
        }
    }

    private static java.util.Map<net.lapidist.colony.components.state.TilePos, RenderTile> buildTileMap(
            final Array<RenderTile> tiles
    ) {
        java.util.Map<net.lapidist.colony.components.state.TilePos, RenderTile> map = new java.util.HashMap<>();
        for (int i = 0; i < tiles.size; i++) {
            RenderTile tile = tiles.get(i);
            map.put(new net.lapidist.colony.components.state.TilePos(tile.getX(), tile.getY()), tile);
        }
        return map;
    }

    private static java.util.Map<net.lapidist.colony.components.state.TilePos, RenderBuilding> buildBuildingMap(
            final Array<RenderBuilding> buildings
    ) {
        java.util.Map<net.lapidist.colony.components.state.TilePos, RenderBuilding> map = new java.util.HashMap<>();
        for (int i = 0; i < buildings.size; i++) {
            RenderBuilding b = buildings.get(i);
            map.put(new net.lapidist.colony.components.state.TilePos(b.getX(), b.getY()), b);
        }
        return map;
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
        return tileMap.get(new net.lapidist.colony.components.state.TilePos(x, y));
    }

    @Override
    public RenderBuilding getBuilding(final int x, final int y) {
        return buildingMap.get(new net.lapidist.colony.components.state.TilePos(x, y));
    }
}
