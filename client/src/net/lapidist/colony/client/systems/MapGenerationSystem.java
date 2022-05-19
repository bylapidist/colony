package net.lapidist.colony.client.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.serialization.MapSerializer;
import net.lapidist.colony.client.core.utils.PathUtils;
import net.lapidist.colony.client.entities.factories.TileFactory;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;

import java.io.IOException;

public class MapGenerationSystem extends EntitySystem {

    private final int mapWidth;

    private final int mapHeight;

    public MapGenerationSystem(final int mapWidthToSet, final int mapHeightToSet) {
        this.mapWidth = mapWidthToSet;
        this.mapHeight = mapHeightToSet;
    }

    @Override
    public final void addedToEngine(final Engine engine) {
        Entity map = new Entity();
        MapComponent mapComponent = new MapComponent();
        MapSerializer serializer = new MapSerializer();
        Array<Entity> tilesToSet = new Array<>();

        if (FileLocation.ABSOLUTE.getFile(PathUtils.getSaveFolder() + "\\save1.json").exists()) {
            try {
                mapComponent = serializer.load("save1.json");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
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

            try {
                serializer.serialize(mapComponent);
                serializer.save("save1.json");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        map.add(mapComponent);
        engine.addEntity(map);
    }

    private String getRandomTextureReference() {
        String[] textures = new String[]{"grass0", "dirt0"};
        return textures[MathUtils.random(0, textures.length - 1)];
    }
}
