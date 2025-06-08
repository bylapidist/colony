package net.lapidist.colony.client.systems.input;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
            final Entity map,
            final ComponentMapper<MapComponent> mapMapper,
            final ComponentMapper<TileComponent> tileMapper
    ) {
        if (map == null) {
            return false;
        }

        cameraSystem.getCamera().update();

        Vector2 worldCoords = CameraUtils.screenToWorldCoords(cameraSystem.getViewport(), x, y);
        Vector2 tileCoords = CameraUtils.worldCoordsToTileCoords(worldCoords);

        Array<Entity> tiles = mapMapper.get(map).getTiles();
        for (int i = 0; i < tiles.size; i++) {
            Entity tile = tiles.get(i);
            TileComponent tileComponent = tileMapper.get(tile);
            if (tileComponent.getX() == (int) tileCoords.x && tileComponent.getY() == (int) tileCoords.y) {
                boolean newState = !tileComponent.isSelected();

                TileSelectionData msg = new TileSelectionData(
                        tileComponent.getX(),
                        tileComponent.getY(),
                        newState
                );
                client.sendTileSelection(msg);
                return true;
            }
        }

        return false;
    }
}
