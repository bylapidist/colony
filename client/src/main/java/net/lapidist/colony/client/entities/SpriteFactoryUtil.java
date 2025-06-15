package net.lapidist.colony.client.entities;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.BoundedComponent;

/**
 * Utility methods shared across entity factories.
 */
public final class SpriteFactoryUtil {

    private SpriteFactoryUtil() {
    }

    public static Entity createEntity(final World world) {
        return world.createEntity();
    }

    public static <T extends com.artemis.Component & BoundedComponent> Entity createEntity(
            final World world,
            final T component,
            final Vector2 coords
    ) {
        Entity entity = createEntity(world);
        component.setHeight(GameConstants.TILE_SIZE);
        component.setWidth(GameConstants.TILE_SIZE);
        component.setX((int) coords.x);
        component.setY((int) coords.y);
        entity.edit().add(component);
        return entity;
    }

    public static <T extends com.artemis.Component & BoundedComponent> Entity createEntity(
            final World world,
            final T component,
            final com.artemis.Component extra,
            final Vector2 coords
    ) {
        Entity entity = createEntity(world, component, coords);
        entity.edit().add(extra);
        return entity;
    }
}
