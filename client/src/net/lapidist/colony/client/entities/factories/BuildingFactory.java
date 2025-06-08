package net.lapidist.colony.client.entities.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.core.Constants;
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
        Entity entity = createEntity(world, resourceRef);
        BuildingComponent buildingComponent = new BuildingComponent();

        buildingComponent.setBuildingType(buildingType);
        buildingComponent.setHeight(Constants.TILE_SIZE);
        buildingComponent.setWidth(Constants.TILE_SIZE);
        buildingComponent.setX((int) coords.x);
        buildingComponent.setY((int) coords.y);
        entity.edit().add(buildingComponent);
        return entity;
    }
}
