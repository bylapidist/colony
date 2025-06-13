package net.lapidist.colony.client.systems.network;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.TileSelectionData;
import net.lapidist.colony.map.MapUtils;

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
        while ((update = client.poll(TileSelectionData.class)) != null) {
            final TileSelectionData data = update;
                    MapUtils.findTile(mapComponent, data.x(), data.y())
                    .ifPresent(t -> {
                        var tc = tileMapper.get(t);
                        tc.setSelected(data.selected());
                        tc.setDirty(true);
                        int index = mapComponent.getTiles().indexOf(t, true);
                        var ds = world.getSystem(net.lapidist.colony.client.systems.MapRenderDataSystem.class);
                        if (ds != null) {
                            ds.addDirtyIndex(index);
                        }
                        mapComponent.incrementVersion();
                    });
        }
    }
}
