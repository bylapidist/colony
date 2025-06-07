package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.entities.factories.TileFactory;
import net.lapidist.colony.client.entities.factories.BuildingFactory;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.map.MapGenerator;

public final class MapGenerationSystem extends BaseSystem {

    private final int mapWidth;

    private final int mapHeight;

    public MapGenerationSystem(final int mapWidthToSet, final int mapHeightToSet) {
        this.mapWidth = mapWidthToSet;
        this.mapHeight = mapHeightToSet;
    }

    @Override
    public void initialize() {
        Array<Entity> tilesToSet = new Array<>();
        Array<Entity> entitiesToSet = new Array<>();
        Entity map = world.createEntity();
        MapComponent mapComponent = new MapComponent();

        var state = MapGenerator.generate(mapWidth, mapHeight);

        for (var td : state.getTiles()) {
            tilesToSet.add(TileFactory.create(
                    world,
                    TileComponent.TileType.valueOf(td.getTileType()),
                    td.getTextureRef(),
                    new Vector2(td.getX(), td.getY()),
                    td.isPassable(),
                    td.isSelected()
            ));
        }

        for (var bd : state.getBuildings()) {
            entitiesToSet.add(BuildingFactory.create(
                    world,
                    BuildingComponent.BuildingType.valueOf(bd.getBuildingType()),
                    bd.getTextureRef(),
                    new Vector2(bd.getX(), bd.getY())
            ));
        }

        mapComponent.setTiles(tilesToSet);
        mapComponent.setEntities(entitiesToSet);
        map.edit().add(mapComponent);
    }

    @Override
    protected void processSystem() {
        // map generation occurs once in initialize
    }

}
