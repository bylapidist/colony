package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.components.state.MapState;
import com.artemis.ComponentMapper;
import com.artemis.Entity;

public final class PlayerCameraSystem extends BaseSystem implements CameraProvider {
    private net.lapidist.colony.client.network.GameClient client;

    public enum Mode { MAP_OVERVIEW, PLAYER }

    private static final float PLAYER_MAX_ZOOM = 1.5f;

    private final Rectangle viewBounds = new Rectangle();

    private Mode mode = Mode.MAP_OVERVIEW;
    private ComponentMapper<PlayerComponent> playerMapper;
    private Entity player;

    private OrthographicCamera camera;

    private ExtendViewport viewport;

    public PlayerCameraSystem() {
        this(null);
    }

    public PlayerCameraSystem(final net.lapidist.colony.client.network.GameClient clientToUse) {
        this.client = clientToUse;
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        // Set camera to center of world
        moveCameraToWorldCoords(getWorldCenter());

        viewport.apply();
    }

    public void setClient(final net.lapidist.colony.client.network.GameClient clientToSet) {
        this.client = clientToSet;
    }

    @Override
    public void initialize() {
        playerMapper = world.getMapper(PlayerComponent.class);
        var players = world.getAspectSubscriptionManager()
                .get(com.artemis.Aspect.all(PlayerComponent.class))
                .getEntities();
        if (players.size() > 0) {
            player = world.getEntity(players.get(0));
        }
    }

    public boolean withinCameraView(final Vector2 worldCoords) {
        return CameraUtils.withinCameraView(viewport, worldCoords);
    }

    public void moveCameraToWorldCoords(final Vector2 worldCoords) {
        viewport.getCamera().translate(worldCoords.x, worldCoords.y, 0);
    }

    public Vector2 getWorldCenter() {
        int width = client != null ? client.getMapWidth() : MapState.DEFAULT_WIDTH;
        int height = client != null ? client.getMapHeight() : MapState.DEFAULT_HEIGHT;
        return CameraUtils.getWorldCenter(width, height);
    }

    public Vector2 cameraCoordsFromWorldCoords(
            final float worldX,
            final float worldY
    ) {
        return CameraUtils.worldToScreenCoords(viewport, worldX, worldY, new Vector2());
    }

    public Vector2 cameraCoordsFromWorldCoords(
            final float worldX,
            final float worldY,
            final Vector2 out
    ) {
        return CameraUtils.worldToScreenCoords(viewport, worldX, worldY, out);
    }

    public Vector2 worldCoordsFromCameraCoords(
            final float screenX,
            final float screenY
    ) {
        return CameraUtils.screenToWorldCoords(viewport, screenX, screenY, new Vector2());
    }

    public Vector2 worldCoordsFromCameraCoords(
            final float screenX,
            final float screenY,
            final Vector2 out
    ) {
        return CameraUtils.screenToWorldCoords(viewport, screenX, screenY, out);
    }

    public Vector2 screenCoordsToWorldCoords(final float screenX, final float screenY) {
        return CameraUtils.screenToWorldCoords(viewport, screenX, screenY, new Vector2());
    }

    public Vector2 screenCoordsToWorldCoords(final float screenX, final float screenY, final Vector2 out) {
        return CameraUtils.screenToWorldCoords(viewport, screenX, screenY, out);
    }

    public Vector2 tileCoordsToWorldCoords(final int x, final int y) {
        return CameraUtils.tileCoordsToWorldCoords(x, y, new Vector2());
    }

    public Vector2 tileCoordsToWorldCoords(final Vector2 coords) {
        return CameraUtils.tileCoordsToWorldCoords(coords, new Vector2());
    }

    public Vector2 tileCoordsToWorldCoords(final int x, final int y, final Vector2 out) {
        return CameraUtils.tileCoordsToWorldCoords(x, y, out);
    }

    public Vector2 tileCoordsToWorldCoords(final Vector2 coords, final Vector2 out) {
        return CameraUtils.tileCoordsToWorldCoords(coords, out);
    }

    public Vector2 worldCoordsToTileCoords(final int x, final int y) {
        return CameraUtils.worldCoordsToTileCoords(x, y, new Vector2());
    }

    public Vector2 worldCoordsToTileCoords(final Vector2 coords) {
        return CameraUtils.worldCoordsToTileCoords(coords, new Vector2());
    }

    public Vector2 worldCoordsToTileCoords(final int x, final int y, final Vector2 out) {
        return CameraUtils.worldCoordsToTileCoords(x, y, out);
    }

    public Vector2 worldCoordsToTileCoords(final Vector2 coords, final Vector2 out) {
        return CameraUtils.worldCoordsToTileCoords(coords, out);
    }

    @Override
    protected void processSystem() {
        if (player == null) {
            var players = world.getAspectSubscriptionManager()
                    .get(com.artemis.Aspect.all(PlayerComponent.class))
                    .getEntities();
            if (players.size() > 0) {
                player = world.getEntity(players.get(0));
            }
        }
        if (mode == Mode.PLAYER && player != null) {
            PlayerComponent pc = playerMapper.get(player);
            camera.position.set(pc.getX(), pc.getY(), 0);
        }
        camera.update();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public float getZoom() {
        return camera.zoom;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(final OrthographicCamera cameraToSet) {
        this.camera = cameraToSet;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(final ExtendViewport viewportToSet) {
        this.viewport = viewportToSet;
    }

    /**
     * Returns the current camera view bounds in world coordinates.
     *
     * @return rectangle representing the visible world area
     */
    public Rectangle getViewBounds() {
        return CameraUtils.getViewBounds(camera, viewport, viewBounds);
    }

    public void toggleMode() {
        mode = mode == Mode.MAP_OVERVIEW ? Mode.PLAYER : Mode.MAP_OVERVIEW;
    }

    public Mode getMode() {
        return mode;
    }

    public boolean isPlayerMode() {
        return mode == Mode.PLAYER;
    }

    public float getMaxZoom() {
        return mode == Mode.PLAYER ? PLAYER_MAX_ZOOM : 2f;
    }
}
