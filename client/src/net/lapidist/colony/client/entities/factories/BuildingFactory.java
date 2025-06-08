package net.lapidist.colony.client.entities.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.entities.BuildingComponent;

import static net.lapidist.colony.client.entities.factories.SpriteFactoryUtil.createEntity;

public final class BuildingFactory {

    private BuildingFactory() {
    }

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
