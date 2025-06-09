package net.lapidist.colony.client.systems.input;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.BuildingPlacementData;

/**
 * Handles player building placement input.
 */
public final class BuildingPlacementHandler {
    private final GameClient client;
    private final PlayerCameraSystem cameraSystem;

    public BuildingPlacementHandler(final GameClient clientToSet, final PlayerCameraSystem cameraSystemToSet) {
        this.client = clientToSet;
        this.cameraSystem = cameraSystemToSet;
    }

    public boolean handleTap(
            final float x,
            final float y,
            final MapComponent map,
            final ComponentMapper<TileComponent> tileMapper
    ) {
        if (map == null) {
            return false;
        }

        cameraSystem.getCamera().update();
        Vector2 worldCoords = CameraUtils.screenToWorldCoords(cameraSystem.getViewport(), x, y);
        Vector2 tileCoords = CameraUtils.worldCoordsToTileCoords(worldCoords);

        Array<Entity> tiles = map.getTiles();
        for (int i = 0; i < tiles.size; i++) {
            Entity tile = tiles.get(i);
            TileComponent tc = tileMapper.get(tile);
            if (tc.getX() == (int) tileCoords.x && tc.getY() == (int) tileCoords.y) {
                BuildingPlacementData msg = new BuildingPlacementData(
                        tc.getX(),
                        tc.getY(),
                        BuildingComponent.BuildingType.HOUSE.name()
                );
                client.sendBuildRequest(msg);
                return true;
            }
        }
        return false;
    }
}
