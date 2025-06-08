package net.lapidist.colony.client.systems.network;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
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
            map = net.lapidist.colony.map.MapUtils.findMapEntity(world).orElse(null);
            if (map == null) {
                return;
            }
        }

        MapComponent mapComponent = mapMapper.get(map);
        TileSelectionData update;
        while ((update = client.pollTileSelection()) != null) {
            for (int i = 0; i < mapComponent.getTiles().size; i++) {
                Entity tile = mapComponent.getTiles().get(i);
                TileComponent tc = tileMapper.get(tile);
                if (tc.getX() == update.x() && tc.getY() == update.y()) {
                    tc.setSelected(update.selected());
                    break;
                }
            }
        }
    }
}
