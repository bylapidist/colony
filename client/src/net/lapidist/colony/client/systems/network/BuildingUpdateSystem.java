package net.lapidist.colony.client.systems.network;

import com.artemis.BaseSystem;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.entities.factories.BuildingFactory;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.map.MapUtils;

/**
 * Applies building placement updates received from the server.
 */
public final class BuildingUpdateSystem extends BaseSystem {
    private final GameClient client;
    private MapComponent map;

    public BuildingUpdateSystem(final GameClient clientToSet) {
        this.client = clientToSet;
    }

    @Override
    protected void processSystem() {
        if (map == null) {
            map = MapUtils.findMap(world).orElse(null);
            if (map == null) {
                return;
            }
        }

        BuildingData update;
        while ((update = client.poll(BuildingData.class)) != null) {
            world.createEntity();
            map.addEntity(BuildingFactory.create(
                    world,
                    BuildingComponent.BuildingType.valueOf(update.buildingType()),
                    update.textureRef(),
                    new Vector2(update.x(), update.y())
            ));
        }
    }
}
