package net.lapidist.colony.client.systems.network;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.resources.ResourceComponent;
import net.lapidist.colony.components.resources.PlayerResourceComponent;
import net.lapidist.colony.components.state.ResourceUpdateData;
import net.lapidist.colony.map.MapUtils;

/** Applies resource updates received from the server. */
public final class ResourceUpdateSystem extends BaseSystem {
    private final GameClient client;
    private ComponentMapper<ResourceComponent> resourceMapper;
    private ComponentMapper<MapComponent> mapMapper;
    private ComponentMapper<TileComponent> tileMapper;
    private ComponentMapper<PlayerResourceComponent> playerMapper;
    private Entity player;
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
        if (player == null) {
            var players = world.getAspectSubscriptionManager()
                    .get(com.artemis.Aspect.all(PlayerResourceComponent.class))
                    .getEntities();
            if (players.size() > 0) {
                player = world.getEntity(players.get(0));
            }
        }
        ResourceUpdateData update;
        while ((update = client.poll(ResourceUpdateData.class)) != null) {
            final ResourceUpdateData data = update;
            if (data.x() == -1 && data.y() == -1) {
                if (player != null) {
                    var pr = playerMapper.get(player);
                    for (var entry : data.amounts().entrySet()) {
                        pr.setAmount(entry.getKey(), entry.getValue());
                    }
                }
                continue;
            }
            MapUtils.findTile(mapComponent, data.x(), data.y())
                    .ifPresent(tile -> {
                        ResourceComponent rc = resourceMapper.get(tile);
                        java.util.Map<String, Integer> before = new java.util.HashMap<>(rc.getAmounts());
                        for (var entry : data.amounts().entrySet()) {
                            rc.setAmount(entry.getKey(), entry.getValue());
                        }
                        rc.setDirty(true);
                        int index = mapComponent.getTiles().indexOf(tile, true);
                        var ds = world.getSystem(net.lapidist.colony.client.systems.MapRenderDataSystem.class);
                        if (ds != null) {
                            ds.addDirtyIndex(index);
                        }
                        if (player != null) {
                            var pr = playerMapper.get(player);
                            for (var entry : data.amounts().entrySet()) {
                                int oldVal = before.getOrDefault(entry.getKey(), 0);
                                int delta = oldVal - entry.getValue();
                                if (delta > 0) {
                                    pr.addAmount(entry.getKey(), delta);
                                }
                            }
                        }
                        mapComponent.incrementVersion();
                    });
        }
    }
}
