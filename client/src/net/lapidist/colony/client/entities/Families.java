package net.lapidist.colony.client.entities;

import com.badlogic.ashley.core.Family;
import net.lapidist.colony.components.*;

public final class Families {

    private Families() {
    }

    public static Family getPlanetFamily() {
        return Family.all(
                PlanetComponent.class,
                PositionComponent.class,
                RenderableComponent.class,
                TextureRegionReferenceComponent.class
        ).get();
    }

    public static Family getStarFamily() {
        return Family.all(
                StarComponent.class,
                PositionComponent.class,
                RenderableComponent.class,
                TextureRegionReferenceComponent.class
        ).get();
    }
}
