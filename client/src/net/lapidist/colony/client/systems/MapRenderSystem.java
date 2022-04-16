package net.lapidist.colony.client.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.entities.factories.TileFactory;
import net.lapidist.colony.components.entities.TileComponent;

public class MapRenderSystem extends EntitySystem {

    private final int mapWidth;

    private final int mapHeight;

    private ImmutableArray<Entity> tiles;

    public MapRenderSystem(final int mapWidthToSet, final int mapHeightToSet) {
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

        this.tiles = engine.getEntitiesFor(
                Family.all(TileComponent.class).get()
        );
    }

    @Override
    public final void update(final float deltaTime) {
        for (int i = 0; i < tiles.size(); ++i) {
            Entity entity = tiles.get(i);
            TileComponent tile = entity.getComponent(TileComponent.class);

            System.out.println(tile.getTileType());
        }
    }
}
