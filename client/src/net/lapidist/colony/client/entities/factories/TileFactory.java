package net.lapidist.colony.client.entities.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.components.entities.TileComponent;

public final class TileFactory {

    private TileFactory() {
    }

    public static Entity create(
            final TileComponent.TileType tileType,
            final Vector2 coords,
            final boolean passable
    ) {
        Entity entity = new Entity();
        TileComponent tileComponent = new TileComponent();

        tileComponent.setTileType(tileType);
        tileComponent.setHeight(Constants.TILE_SIZE);
        tileComponent.setWidth(Constants.TILE_SIZE);
        tileComponent.setPassable(passable);
        tileComponent.setX((int) coords.x);
        tileComponent.setY((int) coords.y);

        entity.add(tileComponent);

        return entity;
    }
}
