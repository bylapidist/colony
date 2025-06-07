package net.lapidist.colony.client.systems.network;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.entities.factories.BuildingFactory;
import net.lapidist.colony.client.entities.factories.TileFactory;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.TileData;

public final class MapLoadSystem extends BaseSystem {
    private final MapState state;

    public MapLoadSystem(final MapState stateToSet) {
        this.state = stateToSet;
    }

    @Override
    public void initialize() {
        Entity map = world.createEntity();
        MapComponent mapComponent = new MapComponent();
        Array<Entity> tiles = new Array<>();
        Array<Entity> entities = new Array<>();

        for (TileData td : state.getTiles()) {
            tiles.add(TileFactory.create(
                    world,
                    TileComponent.TileType.valueOf(td.getTileType()),
                    td.getTextureRef(),
                    new Vector2(td.getX(), td.getY()),
                    td.isPassable(),
                    td.isSelected()
            ));
        }

        for (BuildingData bd : state.getBuildings()) {
            entities.add(BuildingFactory.create(
                    world,
                    BuildingComponent.BuildingType.valueOf(bd.getBuildingType()),
                    bd.getTextureRef(),
                    new Vector2(bd.getX(), bd.getY())
            ));
        }

        mapComponent.setTiles(tiles);
        mapComponent.setEntities(entities);
        map.edit().add(mapComponent);
    }

    @Override
    protected void processSystem() {
        // loading occurs once in initialize
    }
}
