package net.lapidist.colony.client.entities;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.registry.BuildingDefinition;
import net.lapidist.colony.registry.Registries;
import java.util.Locale;

import static net.lapidist.colony.client.entities.SpriteFactoryUtil.createEntity;

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
     * @param coords       spawn coordinates
     * @return created entity
     */
    public static Entity create(
            final World world,
            final String buildingType,
            final Vector2 coords
    ) {
        BuildingComponent buildingComponent = new BuildingComponent();
        BuildingDefinition def = Registries.buildings().get(buildingType);
        String id = def != null
                ? def.id()
                : buildingType == null ? null : buildingType.toLowerCase(Locale.ROOT);
        buildingComponent.setBuildingType(id);
        return createEntity(world, buildingComponent, coords);
    }
}
