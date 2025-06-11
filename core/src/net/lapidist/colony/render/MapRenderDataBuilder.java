package net.lapidist.colony.render;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.render.data.RenderBuilding;
import net.lapidist.colony.render.data.RenderTile;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.resources.ResourceComponent;

/** Utility to convert {@link MapComponent} to {@link MapRenderData}. */
public final class MapRenderDataBuilder {
    private MapRenderDataBuilder() {
    }

    public static MapRenderData fromMap(final MapComponent map, final World world) {
        ComponentMapper<TileComponent> tileMapper = world.getMapper(TileComponent.class);
        ComponentMapper<ResourceComponent> resourceMapper = world.getMapper(ResourceComponent.class);
        ComponentMapper<BuildingComponent> buildingMapper = world.getMapper(BuildingComponent.class);

        Array<RenderTile> tiles = new Array<>();
        Array<Entity> mapTiles = map.getTiles();
        for (int i = 0; i < mapTiles.size; i++) {
            Entity entity = mapTiles.get(i);
            TileComponent tc = tileMapper.get(entity);
            ResourceComponent rc = resourceMapper.get(entity);
            RenderTile tile = RenderTile.builder()
                    .x(tc.getX())
                    .y(tc.getY())
                    .tileType(tc.getTileType().toString())
                    .selected(tc.isSelected())
                    .wood(rc.getWood())
                    .stone(rc.getStone())
                    .food(rc.getFood())
                    .build();
            tiles.add(tile);
        }

        Array<RenderBuilding> buildings = new Array<>();
        Array<Entity> mapEntities = map.getEntities();
        for (int i = 0; i < mapEntities.size; i++) {
            Entity entity = mapEntities.get(i);
            BuildingComponent bc = buildingMapper.get(entity);
            RenderBuilding building = RenderBuilding.builder()
                    .x(bc.getX())
                    .y(bc.getY())
                    .buildingType(bc.getBuildingType().toString())
                    .build();
            buildings.add(building);
        }

        return new SimpleMapRenderData(tiles, buildings);
    }
}
