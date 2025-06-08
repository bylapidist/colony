package net.lapidist.colony.client.systems.network;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.TileSelectionData;

/**
 * Applies tile selection updates received from the server.
 */
public final class TileUpdateSystem extends BaseSystem {
    private final GameClient client;

    private ComponentMapper<MapComponent> mapMapper;
    private ComponentMapper<TileComponent> tileMapper;
    private Entity map;

    public TileUpdateSystem(final GameClient clientToSet) {
        this.client = clientToSet;
    }

    @Override
    protected void processSystem() {
        if (map == null) {
            IntBag maps = world.getAspectSubscriptionManager()
                    .get(Aspect.all(MapComponent.class))
                    .getEntities();
            if (maps.size() == 0) {
                return;
            }
            map = world.getEntity(maps.get(0));
        }

        MapComponent mapComponent = mapMapper.get(map);
        TileSelectionData update;
        while ((update = client.pollTileSelection()) != null) {
            for (int i = 0; i < mapComponent.getTiles().size; i++) {
                Entity tile = mapComponent.getTiles().get(i);
                TileComponent tc = tileMapper.get(tile);
                if (tc.getX() == update.getX() && tc.getY() == update.getY()) {
                    tc.setSelected(update.isSelected());
                    break;
                }
            }
        }
    }
}
