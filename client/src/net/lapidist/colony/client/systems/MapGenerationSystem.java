package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.entities.factories.TileFactory;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;

public class MapGenerationSystem extends BaseSystem {

    private final int mapWidth;

    private final int mapHeight;

    public MapGenerationSystem(final int mapWidthToSet, final int mapHeightToSet) {
        this.mapWidth = mapWidthToSet;
        this.mapHeight = mapHeightToSet;
    }

    @Override
    protected void processSystem() {
    }

    @Override
    protected void initialize() {
    }

    public final void generate() {
        Array<Entity> tilesToSet = new Array<>();
        Entity map = new Entity();
        MapComponent mapComponent = new MapComponent();

        for (int column = 0; column <= mapWidth; column++) {
            for (int row = 0; row <= mapHeight; row++) {
                tilesToSet.add(TileFactory.create(
                        TileComponent.TileType.GRASS,
                        getRandomTextureReference(),
                        new Vector2(column, row),
                        true
                ));
            }
        }

        mapComponent.setTiles(tilesToSet);
        map.add(mapComponent);
        engine.addEntity(map);
    }

    private String getRandomTextureReference() {
        String[] textures = new String[]{"grass0", "dirt0"};
        return textures[MathUtils.random(0, textures.length - 1)];
    }
}
