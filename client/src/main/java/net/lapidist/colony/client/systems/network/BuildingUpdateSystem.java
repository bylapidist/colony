package net.lapidist.colony.client.systems.network;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.entities.BuildingFactory;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.BuildingData;
import net.lapidist.colony.components.state.BuildingRemovalData;
import net.lapidist.colony.map.MapUtils;

/**
 * Applies building placement updates received from the server.
 */
public final class BuildingUpdateSystem extends BaseSystem {
    private final GameClient client;
    private MapComponent map;
    private ComponentMapper<BuildingComponent> buildingMapper;

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
            var entity = BuildingFactory.create(
                    world,
                    update.buildingType(),
                    new Vector2(update.x(), update.y())
            );
            buildingMapper.get(entity).setDirty(true);
            map.addEntity(entity);
            map.incrementVersion();
        }

        BuildingRemovalData removal;
        while ((removal = client.poll(BuildingRemovalData.class)) != null) {
            for (int i = 0; i < map.getEntities().size; i++) {
                var entity = map.getEntities().get(i);
                BuildingComponent bc = buildingMapper.get(entity);
                if (bc.getX() == removal.x() && bc.getY() == removal.y()) {
                    map.removeEntity(entity);
                    entity.deleteFromWorld();
                    break;
                }
            }
        }
    }
}
