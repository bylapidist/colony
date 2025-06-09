package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.input.BuildingPlacementHandler;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.map.MapUtils;

/**
 * Handles player building placement input.
 */
public final class BuildPlacementSystem extends BaseSystem {

    private final GameClient client;
    private PlayerCameraSystem cameraSystem;
    private MapComponent map;
    private ComponentMapper<TileComponent> tileMapper;
    private BuildingPlacementHandler handler;
    private boolean buildMode;

    public BuildPlacementSystem(final GameClient clientToSet) {
        this.client = clientToSet;
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        tileMapper = world.getMapper(TileComponent.class);
        map = MapUtils.findMap(world).orElse(null);
        handler = new BuildingPlacementHandler(client, cameraSystem);
    }

    @Override
    protected void processSystem() {
        if (map == null) {
            map = MapUtils.findMap(world).orElse(null);
        }
    }

    public void setBuildMode(final boolean mode) {
        this.buildMode = mode;
    }

    public boolean tap(final float x, final float y, final int count, final int button) {
        if (!buildMode) {
            return false;
        }
        return handler.handleTap(x, y, map, tileMapper);
    }
}
