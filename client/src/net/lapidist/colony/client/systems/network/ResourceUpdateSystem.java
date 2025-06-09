package net.lapidist.colony.client.systems.network;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.resources.ResourceComponent;
import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.map.MapUtils;

/** Applies resource updates received from the server. */
public final class ResourceUpdateSystem extends BaseSystem {
    private final GameClient client;
    private ComponentMapper<ResourceComponent> resourceMapper;
    private ComponentMapper<MapComponent> mapMapper;
    private Entity map;

    public ResourceUpdateSystem(final GameClient clientToUse) {
        this.client = clientToUse;
    }

    @Override
    protected void processSystem() {
        if (map == null) {
            map = MapUtils.findMapEntity(world).orElse(null);
            if (map == null) {
                return;
            }
        }
        MapComponent mapComponent = mapMapper.get(map);
        ResourceUpdateData update;
        while ((update = client.pollResourceUpdate()) != null) {
            for (int i = 0; i < mapComponent.getTiles().size; i++) {
                Entity tile = mapComponent.getTiles().get(i);
                ResourceComponent rc = resourceMapper.get(tile);
                var tcMapper = world.getMapper(net.lapidist.colony.components.maps.TileComponent.class);
                net.lapidist.colony.components.maps.TileComponent tc = tcMapper.get(tile);
                if (tc.getX() == update.x() && tc.getY() == update.y()) {
                    rc.setWood(update.wood());
                    rc.setStone(update.stone());
                    rc.setFood(update.food());
                    break;
                }
            }
        }
    }
}
