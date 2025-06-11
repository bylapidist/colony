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
import net.lapidist.colony.components.state.TilePos;
import net.lapidist.colony.client.renderers.AssetResolver;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;

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
        return create(world, state, new DefaultAssetResolver());
    }

    public static Entity create(
            final World world,
            final MapState state,
            final AssetResolver resolver
    ) {
        Entity map = world.createEntity();
        MapComponent mapComponent = new MapComponent();
        Array<Entity> tiles = new Array<>();
        Map<TilePos, Entity> tileMap = new HashMap<>();
        Array<Entity> entities = new Array<>();

        for (Map.Entry<TilePos, TileData> entry : state.tiles().entrySet()) {
            TileData td = entry.getValue();
            String texture = resolver.tileAsset(TileComponent.TileType.valueOf(td.tileType()));
            Entity tile = TileFactory.create(
                    world,
                    TileComponent.TileType.valueOf(td.tileType()),
                    texture,
                    new Vector2(td.x(), td.y()),
                    td.passable(),
                    td.selected(),
                    td.resources()
            );
            tiles.add(tile);
            tileMap.put(entry.getKey(), tile);
        }

        for (BuildingData bd : state.buildings()) {
            String texture = resolver.buildingAsset(
                    BuildingComponent.BuildingType.valueOf(bd.buildingType()));
            entities.add(BuildingFactory.create(
                    world,
                    BuildingComponent.BuildingType.valueOf(bd.buildingType()),
                    texture,
                    new Vector2(bd.x(), bd.y())
            ));
        }

        mapComponent.setTiles(tiles);
        mapComponent.setTileMap(tileMap);
        mapComponent.setEntities(entities);
        map.edit().add(mapComponent);
        return map;
    }
}
