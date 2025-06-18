package net.lapidist.colony.map;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.resources.ResourceComponent;
import net.lapidist.colony.components.state.map.BuildingData;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.map.TileData;
import net.lapidist.colony.components.state.map.TilePos;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.registry.TileDefinition;
import net.lapidist.colony.registry.BuildingDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility factory for creating map entities from {@link MapState} data.
 */
public final class MapFactory {

    private MapFactory() { }

    /**
     * Create a new map entity populated with tiles and buildings from the given state.
     *
     * @param world the Artemis world to create entities in
     * @param state the map state to convert to entities
     * @return the created map entity containing a {@link MapComponent}
     */
    public static Entity create(final World world, final MapState state) {
        Entity map = world.createEntity();
        MapComponent mapComponent = new MapComponent();
        Array<Entity> tiles = new Array<>();
        Map<TilePos, Entity> tileMap = new HashMap<>();
        Array<Entity> entities = new Array<>();

        for (int x = 0; x < state.width(); x++) {
            for (int y = 0; y < state.height(); y++) {
                TileData td = state.getTile(x, y);
                Entity tile = world.createEntity();
                TileComponent tileComponent = new TileComponent();
                String tileType = td.tileType();
                TileDefinition tileDef = Registries.tiles().get(tileType);
                if (tileDef == null) {
                    tileDef = Registries.tiles().get("empty");
                }
                tileComponent.setTileType(tileDef.id());
                tileComponent.setPassable(td.passable());
                tileComponent.setSelected(td.selected());
                tileComponent.setHeight(GameConstants.TILE_SIZE);
                tileComponent.setWidth(GameConstants.TILE_SIZE);
                tileComponent.setX(td.x());
                tileComponent.setY(td.y());
                tile.edit().add(tileComponent);


                ResourceComponent rc = new ResourceComponent();
                if (td.resources() != null) {
                    rc.setAmount("WOOD", td.resources().wood());
                    rc.setAmount("STONE", td.resources().stone());
                    rc.setAmount("FOOD", td.resources().food());
                }
                tile.edit().add(rc);

                tiles.add(tile);
                tileMap.put(new TilePos(td.x(), td.y()), tile);
            }
        }

        for (BuildingData bd : state.buildings()) {
            String buildingType = bd.buildingType();
            BuildingDefinition def = Registries.buildings().get(buildingType);
            if (def == null) {
                continue;
            }
            Entity building = world.createEntity();
            BuildingComponent component = new BuildingComponent();
            component.setBuildingType(def.id());
            component.setHeight(GameConstants.TILE_SIZE);
            component.setWidth(GameConstants.TILE_SIZE);
            component.setX(bd.x());
            component.setY(bd.y());
            building.edit().add(component);
            entities.add(building);
        }

        mapComponent.setTiles(tiles);
        mapComponent.setTileMap(tileMap);
        mapComponent.setEntities(entities);
        map.edit().add(mapComponent);
        mapComponent.incrementVersion();
        return map;
    }
}
