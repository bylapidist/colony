package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.math.Rectangle;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.MapChunkRequest;
import net.lapidist.colony.components.state.ChunkPos;
import net.lapidist.colony.map.MapCoordinateUtils;
import net.lapidist.colony.components.state.MapState;

/**
 * Requests map chunks near the camera position.
 */
public final class ChunkLoadSystem extends BaseSystem {
    private final GameClient client;
    private PlayerCameraSystem cameraSystem;
    private final Rectangle view = new Rectangle();
    private boolean requestedInitial;

    public ChunkLoadSystem(final GameClient clientToUse) {
        this.client = clientToUse;
    }

    @Override
    public void initialize() {
        cameraSystem = world.getSystem(PlayerCameraSystem.class);
    }

    @Override
    protected void processSystem() {
        if (cameraSystem == null) {
            cameraSystem = world.getSystem(PlayerCameraSystem.class);
            if (cameraSystem == null) {
                return;
            }
        }
        MapState state = client.getMapState();
        if (state == null) {
            return;
        }
        if (!requestedInitial && state.playerPos() != null) {
            int cx = MapCoordinateUtils.toChunkCoord(state.playerPos().x());
            int cy = MapCoordinateUtils.toChunkCoord(state.playerPos().y());
            client.send(new MapChunkRequest(cx, cy));
            requestedInitial = true;
        }
        view.set(cameraSystem.getViewBounds());
        int centerX = Math.round(view.x + view.width / 2f) / GameConstants.TILE_SIZE;
        int centerY = Math.round(view.y + view.height / 2f) / GameConstants.TILE_SIZE;
        int chunkX = MapCoordinateUtils.toChunkCoord(centerX);
        int chunkY = MapCoordinateUtils.toChunkCoord(centerY);
        int radius = GameConstants.CHUNK_LOAD_RADIUS;
        for (int x = chunkX - radius; x <= chunkX + radius; x++) {
            for (int y = chunkY - radius; y <= chunkY + radius; y++) {
                ChunkPos pos = new ChunkPos(x, y);
                if (!state.chunks().containsKey(pos)) {
                    client.send(new MapChunkRequest(x, y));
                }
            }
        }
    }
}
