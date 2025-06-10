package net.lapidist.colony.client.entities.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.entities.BuildingComponent;

import static net.lapidist.colony.client.entities.factories.SpriteFactoryUtil.createEntity;

/**
 * Factory methods for creating building entities.
 */
public final class BuildingFactory {

    private BuildingFactory() {
    }

    /**
     * Create a new building entity.
     *
     * @param world        the Artemis world the entity belongs to
     * @param buildingType the building type
     * @param resourceRef  texture or atlas reference for the sprite
     * @param coords       spawn coordinates
     * @return created entity
     */
    public static Entity create(
            final World world,
            final BuildingComponent.BuildingType buildingType,
            final String resourceRef,
            final Vector2 coords
    ) {
        BuildingComponent buildingComponent = new BuildingComponent();
        buildingComponent.setBuildingType(buildingType);
        return createEntity(world, resourceRef, buildingComponent, coords);
    }
}
