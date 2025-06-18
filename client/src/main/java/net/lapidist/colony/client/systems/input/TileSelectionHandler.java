package net.lapidist.colony.client.systems.input;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.map.MapUtils;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.state.TileSelectionData;

/**
 * Processes tile selection input events.
 */
public final class TileSelectionHandler {

    private final GameClient client;
    private final PlayerCameraSystem cameraSystem;
    private final Array<Entity> selectedTiles;
    private final MapRenderDataSystem dataSystem;

    public TileSelectionHandler(
            final GameClient clientToSet,
            final PlayerCameraSystem cameraSystemToSet,
            final Array<Entity> selectedTilesToUse,
            final MapRenderDataSystem dataSystemToUse
    ) {
        this.client = clientToSet;
        this.cameraSystem = cameraSystemToSet;
        this.selectedTiles = selectedTilesToUse;
        this.dataSystem = dataSystemToUse;
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

        return MapUtils.findTile(map, (int) tileCoords.x, (int) tileCoords.y)
                .map(tile -> {
                    TileComponent tileComponent = tileMapper.get(tile);

                    if (!tileComponent.isSelected()) {
                        for (int i = 0; i < selectedTiles.size; i++) {
                            Entity selected = selectedTiles.get(i);
                            TileComponent comp = tileMapper.get(selected);
                            if (comp.isSelected()) {
                                client.sendTileSelectionRequest(new TileSelectionData(
                                        comp.getX(),
                                        comp.getY(),
                                        false
                                ));
                                comp.setSelected(false);
                                comp.setDirty(true);
                                int idx = map.getTiles().indexOf(selected, true);
                                if (idx != -1) {
                                    dataSystem.addDirtyIndex(idx);
                                }
                                map.incrementVersion();
                            }
                        }
                        selectedTiles.clear();
                    }

                    boolean newState = !tileComponent.isSelected();

                    TileSelectionData msg = new TileSelectionData(
                            tileComponent.getX(),
                            tileComponent.getY(),
                            newState
                    );
                    client.sendTileSelectionRequest(msg);
                    tileComponent.setSelected(newState);
                    tileComponent.setDirty(true);
                    int idx = map.getTiles().indexOf(tile, true);
                    if (idx != -1) {
                        dataSystem.addDirtyIndex(idx);
                    }
                    map.incrementVersion();

                    if (newState) {
                        selectedTiles.add(tile);
                    } else {
                        selectedTiles.removeValue(tile, true);
                    }
                    return true;
                })
                .orElse(false);
    }
}
