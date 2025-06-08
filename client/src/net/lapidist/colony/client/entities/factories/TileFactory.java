package net.lapidist.colony.client.entities.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.maps.TileComponent;

import static net.lapidist.colony.client.entities.factories.SpriteFactoryUtil.createEntity;

public final class TileFactory {

    private TileFactory() {
    }

    public static Entity create(
            final World world,
            final TileComponent.TileType tileType,
            final String resourceRef,
            final Vector2 coords,
            final boolean passable,
            final boolean selected
    ) {
        TileComponent tileComponent = new TileComponent();
        tileComponent.setTileType(tileType);
        tileComponent.setPassable(passable);
        tileComponent.setSelected(selected);

        return createEntity(world, resourceRef, tileComponent, coords);
    }
}
