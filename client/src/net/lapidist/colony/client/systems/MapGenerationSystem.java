package net.lapidist.colony.client.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.entities.factories.TileFactory;
import net.lapidist.colony.components.entities.TileComponent;

public class MapGenerationSystem extends EntitySystem {

    private final int mapWidth;

    private final int mapHeight;

    public MapGenerationSystem(final int mapWidthToSet, final int mapHeightToSet) {
        this.mapWidth = mapWidthToSet;
        this.mapHeight = mapHeightToSet;
    }

    @Override
    public final void addedToEngine(final Engine engine) {
        for (int column = 0; column <= mapWidth; column++) {
            for (int row = 0; row <= mapHeight; row++) {
                Entity tile = TileFactory.create(
                        TileComponent.TileType.EMPTY,
                        new Vector2(0, 0),
                        true
                );

                engine.addEntity(tile);
            }
        }
    }
}
