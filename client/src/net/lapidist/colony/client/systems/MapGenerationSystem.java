package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.entities.factories.TileFactory;
import net.lapidist.colony.client.entities.factories.BuildingFactory;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;

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

        for (int column = 0; column <= mapWidth; column++) {
            for (int row = 0; row <= mapHeight; row++) {
                tilesToSet.add(TileFactory.create(
                        world,
                        TileComponent.TileType.GRASS,
                        getRandomTextureReference(),
                        new Vector2(column, row),
                        true,
                        false
                ));
            }
        }

        entitiesToSet.add(BuildingFactory.create(
                world,
                BuildingComponent.BuildingType.HOUSE,
                "house0",
                new Vector2(1, 1)
        ));

        mapComponent.setTiles(tilesToSet);
        mapComponent.setEntities(entitiesToSet);
        map.edit().add(mapComponent);
    }

    @Override
    protected void processSystem() {
        // map generation occurs once in initialize
    }

    private String getRandomTextureReference() {
        String[] textures = new String[]{"grass0", "dirt0"};
        return textures[MathUtils.random(0, textures.length - 1)];
    }
}
