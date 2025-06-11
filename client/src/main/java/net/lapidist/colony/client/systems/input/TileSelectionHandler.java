package net.lapidist.colony.client.systems.input;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.map.MapUtils;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.TileSelectionData;

/**
 * Processes tile selection input events.
 */
public final class TileSelectionHandler {

    private final GameClient client;
    private final PlayerCameraSystem cameraSystem;

    public TileSelectionHandler(
            final GameClient clientToSet,
            final PlayerCameraSystem cameraSystemToSet
    ) {
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

        return MapUtils.findTile(map, (int) tileCoords.x, (int) tileCoords.y, tileMapper)
                .map(tile -> {
                    TileComponent tileComponent = tileMapper.get(tile);
                    boolean newState = !tileComponent.isSelected();

                    TileSelectionData msg = new TileSelectionData(
                            tileComponent.getX(),
                            tileComponent.getY(),
                            newState
                    );
                    client.sendTileSelectionRequest(msg);
                    return true;
                })
                .orElse(false);
    }
}
