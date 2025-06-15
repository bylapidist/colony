package net.lapidist.colony.client.systems.input;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.state.BuildingRemovalData;

/** Handles player building removal input. */
public final class BuildingRemovalHandler {
    private final GameClient client;
    private final PlayerCameraSystem cameraSystem;

    public BuildingRemovalHandler(final GameClient clientToUse, final PlayerCameraSystem cameraSystemToUse) {
        this.client = clientToUse;
        this.cameraSystem = cameraSystemToUse;
    }

    public boolean handleTap(
            final float x,
            final float y,
            final MapComponent map,
            final ComponentMapper<BuildingComponent> buildingMapper
    ) {
        if (map == null) {
            return false;
        }
        cameraSystem.getCamera().update();
        Vector2 worldCoords = CameraUtils.screenToWorldCoords(cameraSystem.getViewport(), x, y);
        int tileX = (int) (worldCoords.x / GameConstants.TILE_SIZE);
        int tileY = (int) (worldCoords.y / GameConstants.TILE_SIZE);
        for (int i = 0; i < map.getEntities().size; i++) {
            var entity = map.getEntities().get(i);
            BuildingComponent bc = buildingMapper.get(entity);
            if (bc.getX() == tileX && bc.getY() == tileY) {
                client.sendRemoveBuildingRequest(new BuildingRemovalData(tileX, tileY));
                return true;
            }
        }
        return false;
    }
}
