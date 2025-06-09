package net.lapidist.colony.client.entities.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;

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
        Array<Entity> entities = new Array<>();

        for (TileData td : state.tiles().values()) {
            tiles.add(TileFactory.create(
                    world,
                    TileComponent.TileType.valueOf(td.tileType()),
                    td.textureRef(),
                    new Vector2(td.x(), td.y()),
                    td.passable(),
                    td.selected(),
                    td.resources()
            ));
        }

        for (BuildingData bd : state.buildings()) {
            entities.add(BuildingFactory.create(
                    world,
                    BuildingComponent.BuildingType.valueOf(bd.buildingType()),
                    bd.textureRef(),
                    new Vector2(bd.x(), bd.y())
            ));
        }

        mapComponent.setTiles(tiles);
        mapComponent.setEntities(entities);
        map.edit().add(mapComponent);
        return map;
    }
}
