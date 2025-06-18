package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.lapidist.colony.client.graphics.CameraUtils;
import net.lapidist.colony.components.entities.PlayerComponent;
import net.lapidist.colony.components.state.map.MapState;
import com.artemis.ComponentMapper;
import com.artemis.Entity;

public final class PlayerCameraSystem extends BaseSystem implements CameraProvider {
    private net.lapidist.colony.client.network.GameClient client;

    public enum Mode { MAP_OVERVIEW, PLAYER }

    private static final float PLAYER_MAX_ZOOM = 1.5f;

    private final Rectangle viewBounds = new Rectangle();

    private Mode mode = Mode.PLAYER;
    private ComponentMapper<PlayerComponent> playerMapper;
    private Entity player;

    private OrthographicCamera camera;

    private ExtendViewport viewport;

    private static final float MIN_ZOOM = 0.5f;
    private static final float DEFAULT_LERP_SPEED = 0f;

    private final Vector2 targetPosition = new Vector2();
    private float targetZoom;
    private float lerpSpeed = DEFAULT_LERP_SPEED;

    public PlayerCameraSystem() {
        this(null);
    }

    public PlayerCameraSystem(final net.lapidist.colony.client.network.GameClient clientToUse) {
        this.client = clientToUse;
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        // Set camera to center of world
        moveCameraToWorldCoords(getWorldCenter());
        targetPosition.set(camera.position.x, camera.position.y);
        targetZoom = camera.zoom;

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
        targetPosition.add(worldCoords);
        if (lerpSpeed <= 0f) {
            camera.position.set(targetPosition, 0f);
        }
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
            targetPosition.set(pc.getX(), pc.getY());
        }

        float alpha = lerpSpeed > 0f ? Math.min(1f, lerpSpeed * world.getDelta()) : 1f;
        camera.position.lerp(new Vector3(targetPosition.x, targetPosition.y, 0), alpha);
        camera.zoom = MathUtils.lerp(camera.zoom, targetZoom, alpha);

        camera.update();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void setSmoothingSpeed(final float speed) {
        this.lerpSpeed = speed;
    }

    public float getSmoothingSpeed() {
        return lerpSpeed;
    }

    public void translate(final float deltaX, final float deltaY) {
        targetPosition.add(deltaX, deltaY);
        if (lerpSpeed <= 0f) {
            camera.position.add(deltaX, deltaY, 0);
        }
    }

    public void setTargetPosition(final Vector2 pos) {
        targetPosition.set(pos);
        if (lerpSpeed <= 0f) {
            camera.position.set(pos.x, pos.y, 0);
        }
    }

    public void setZoom(final float zoom) {
        targetZoom = MathUtils.clamp(zoom, MIN_ZOOM, getMaxZoom());
        if (lerpSpeed <= 0f) {
            camera.zoom = targetZoom;
        }
    }

    public Vector2 getTargetPosition() {
        return targetPosition;
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
        setZoom(MathUtils.clamp(targetZoom, MIN_ZOOM, getMaxZoom()));
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
