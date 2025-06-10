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
        while ((update = client.pollResourceUpdate()) != null) {
            final ResourceUpdateData data = update;
            MapUtils.findTile(mapComponent, data.x(), data.y(), tileMapper)
                    .ifPresent(tile -> {
                        ResourceComponent rc = resourceMapper.get(tile);
                        int deltaWood = rc.getWood() - data.wood();
                        int deltaStone = rc.getStone() - data.stone();
                        int deltaFood = rc.getFood() - data.food();
                        rc.setWood(data.wood());
                        rc.setStone(data.stone());
                        rc.setFood(data.food());
                        if (player != null) {
                            var pr = playerMapper.get(player);
                            if (deltaWood > 0) {
                                pr.addWood(deltaWood);
                            }
                            if (deltaStone > 0) {
                                pr.addStone(deltaStone);
                            }
                            if (deltaFood > 0) {
                                pr.addFood(deltaFood);
                            }
                        }
                    });
        }
    }
}
