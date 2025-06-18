package net.lapidist.colony.client.render;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.resources.ResourceComponent;
import net.lapidist.colony.components.state.map.MapState;

/** Utility to convert {@link MapComponent} to {@link MapRenderData}. */
public final class MapRenderDataBuilder {
    private MapRenderDataBuilder() {
    }

    public static MapRenderData fromMap(final MapComponent map, final World world) {
        return fromMap(map, world, MapState.DEFAULT_WIDTH, MapState.DEFAULT_HEIGHT);
    }

    public static MapRenderData fromMap(
            final MapComponent map,
            final World world,
            final int width,
            final int height
    ) {
        ComponentMapper<TileComponent> tileMapper = world.getMapper(TileComponent.class);
        ComponentMapper<ResourceComponent> resourceMapper = world.getMapper(ResourceComponent.class);
        ComponentMapper<BuildingComponent> buildingMapper = world.getMapper(BuildingComponent.class);

        Array<RenderTile> tiles = new Array<>();
        RenderTile[][] grid = new RenderTile[width][height];
        Array<Entity> mapTiles = map.getTiles();
        for (int i = 0; i < mapTiles.size; i++) {
            Entity entity = mapTiles.get(i);
            TileComponent tc = tileMapper.get(entity);
            ResourceComponent rc = resourceMapper.get(entity);
            RenderTile tile = toTile(tc, rc);
            tiles.add(tile);
            if (tc.getX() >= 0 && tc.getX() < width
                    && tc.getY() >= 0 && tc.getY() < height) {
                grid[tc.getX()][tc.getY()] = tile;
            }
        }

        Array<RenderBuilding> buildings = new Array<>();
        Array<Entity> mapEntities = map.getEntities();
        for (int i = 0; i < mapEntities.size; i++) {
            Entity entity = mapEntities.get(i);
            BuildingComponent bc = buildingMapper.get(entity);
            RenderBuilding building = toBuilding(bc);
            buildings.add(building);
        }

        SimpleMapRenderData data = new SimpleMapRenderData(tiles, buildings, grid);
        data.setVersion(map.getVersion());
        return data;
    }

    public static RenderTile toTile(final TileComponent tc, final ResourceComponent rc) {
        return RenderTile.builder()
                .x(tc.getX())
                .y(tc.getY())
                .tileType(tc.getTileType())
                .selected(tc.isSelected())
                .wood(rc.getWood())
                .stone(rc.getStone())
                .food(rc.getFood())
                .build();
    }

    public static RenderBuilding toBuilding(final BuildingComponent bc) {
        return RenderBuilding.builder()
                .x(bc.getX())
                .y(bc.getY())
                .buildingType(bc.getBuildingType())
                .build();
    }

    /**
     * Update an existing {@link SimpleMapRenderData} with new tile values.
     * The provided indices list determines which tiles should be refreshed.
     */
    public static void updateTiles(
            final MapComponent map,
            final World world,
            final SimpleMapRenderData data,
            final com.badlogic.gdx.utils.IntArray indices
    ) {
        ComponentMapper<TileComponent> tileMapper = world.getMapper(TileComponent.class);
        ComponentMapper<ResourceComponent> resourceMapper = world.getMapper(ResourceComponent.class);

        Array<Entity> mapTiles = map.getTiles();
        if (indices == null) {
            for (int i = 0; i < mapTiles.size; i++) {
                Entity entity = mapTiles.get(i);
                TileComponent tc = tileMapper.get(entity);
                ResourceComponent rc = resourceMapper.get(entity);
                RenderTile tile = toTile(tc, rc);
                data.updateTile(i, tile);
            }
        } else {
            for (int j = 0; j < indices.size; j++) {
                int i = indices.get(j);
                if (i < 0 || i >= mapTiles.size) {
                    continue;
                }
                Entity entity = mapTiles.get(i);
                TileComponent tc = tileMapper.get(entity);
                ResourceComponent rc = resourceMapper.get(entity);
                RenderTile tile = toTile(tc, rc);
                data.updateTile(i, tile);
            }
        }

        data.setVersion(map.getVersion());
    }

    /**
     * Update all tiles and buildings of an existing {@link SimpleMapRenderData} instance.
     * This method recreates every render element and should be used sparingly.
     */
    public static void update(final MapComponent map, final World world, final SimpleMapRenderData data) {
        updateTiles(map, world, data, null);

        ComponentMapper<BuildingComponent> buildingMapper = world.getMapper(BuildingComponent.class);
        Array<Entity> mapEntities = map.getEntities();
        Array<RenderBuilding> buildings = data.getBuildings();
        buildings.clear();
        for (int i = 0; i < mapEntities.size; i++) {
            Entity entity = mapEntities.get(i);
            BuildingComponent bc = buildingMapper.get(entity);
            RenderBuilding building = toBuilding(bc);
            buildings.add(building);
        }

        data.setVersion(map.getVersion());
    }
}
