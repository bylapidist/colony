package net.lapidist.colony.client.entities.factories;

import com.artemis.Entity;
import com.artemis.World;
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
}
