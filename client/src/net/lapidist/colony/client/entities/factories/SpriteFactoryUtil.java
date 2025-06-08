package net.lapidist.colony.client.entities.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.components.BoundedComponent;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;

/**
 * Utility methods shared across entity factories.
 */
public final class SpriteFactoryUtil {

    private SpriteFactoryUtil() {
    }

    public static Entity createEntity(final World world, final String resourceRef) {
        Entity entity = world.createEntity();
        TextureRegionReferenceComponent texture = new TextureRegionReferenceComponent();
        texture.setResourceRef(resourceRef);
        entity.edit().add(texture);
        return entity;
    }

    public static <T extends com.artemis.Component & BoundedComponent> Entity createEntity(
            final World world,
            final String resourceRef,
            final T component,
            final Vector2 coords
    ) {
        Entity entity = createEntity(world, resourceRef);
        component.setHeight(Constants.TILE_SIZE);
        component.setWidth(Constants.TILE_SIZE);
        component.setX((int) coords.x);
        component.setY((int) coords.y);
        entity.edit().add(component);
        return entity;
    }
}
