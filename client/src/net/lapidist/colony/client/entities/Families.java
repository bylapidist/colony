package net.lapidist.colony.client.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import net.lapidist.colony.components.*;

public final class Families {

    private Families() {
    }

    public static Family createPlanetFamily() {
        return Family.all(
                PlanetComponent.class,
                PositionComponent.class,
                OrbitalRadiusComponent.class,
                RenderableComponent.class,
                TextureRegionReferenceComponent.class
        ).get();
    }

    public static Family createStarFamily() {
        return Family.all(
                StarComponent.class,
                PositionComponent.class,
                OrbitalRadiusComponent.class,
                RenderableComponent.class,
                TextureRegionReferenceComponent.class
        ).get();
    }
}
