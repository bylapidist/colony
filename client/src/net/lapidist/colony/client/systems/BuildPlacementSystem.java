package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.input.BuildingPlacementHandler;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.map.MapUtils;

/**
 * Handles building placement when build mode is enabled.
 */
public final class BuildPlacementSystem extends BaseSystem {

    private final GameClient client;
    private boolean buildMode;

    private PlayerCameraSystem cameraSystem;
    private MapComponent map;
    private ComponentMapper<TileComponent> tileMapper;
    private BuildingPlacementHandler buildingPlacementHandler;

    public BuildPlacementSystem(final GameClient clientToUse) {
        this.client = clientToUse;
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        tileMapper = world.getMapper(TileComponent.class);
        map = MapUtils.findMap(world).orElse(null);
        buildingPlacementHandler = new BuildingPlacementHandler(client, cameraSystem);
    }

    @Override
    protected void processSystem() {
        if (map == null) {
            map = MapUtils.findMap(world).orElse(null);
        }
    }

    public boolean tap(final float x, final float y) {
        if (!buildMode) {
            return false;
        }
        return buildingPlacementHandler.handleTap(x, y, map, tileMapper);
    }

    public void setBuildMode(final boolean mode) {
        this.buildMode = mode;
    }
}
