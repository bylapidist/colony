package net.lapidist.colony.client.entities.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.entities.BuildingComponent;

public final class BuildingFactory {

    private BuildingFactory() {
    }

    public static Entity create(
            final BuildingComponent.BuildingType buildingType,
            final String resourceRef,
            final Vector2 coords
    ) {
        Entity entity = new Entity();
        BuildingComponent buildingComponent = new BuildingComponent();
        TextureRegionReferenceComponent textureRegionReferenceComponent = new TextureRegionReferenceComponent();

        textureRegionReferenceComponent.setResourceRef(resourceRef);
        buildingComponent.setBuildingType(buildingType);
        buildingComponent.setHeight(Constants.TILE_SIZE);
        buildingComponent.setWidth(Constants.TILE_SIZE);
        buildingComponent.setX((int) coords.x);
        buildingComponent.setY((int) coords.y);

        entity.add(buildingComponent);
        entity.add(textureRegionReferenceComponent);

        return entity;
    }
}
