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

public final class PlayerCameraSystem extends BaseSystem implements CameraProvider {

    private final Rectangle viewBounds = new Rectangle();

    private OrthographicCamera camera;

    private ExtendViewport viewport;

    public PlayerCameraSystem() {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        // Set camera to center of world
        moveCameraToWorldCoords(getWorldCenter());

        viewport.apply();
    }

    public boolean withinCameraView(final Vector2 worldCoords) {
        return CameraUtils.withinCameraView(viewport, worldCoords);
    }

    public void moveCameraToWorldCoords(final Vector2 worldCoords) {
        viewport.getCamera().translate(worldCoords.x, worldCoords.y, 0);
    }

    public Vector2 getWorldCenter() {
        return CameraUtils.getWorldCenter();
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
        camera.update();
    }

    @net.mostlyoriginal.api.event.common.Subscribe
    private void onResize(final net.lapidist.colony.client.events.ResizeEvent event) {
        viewport.update(event.width(), event.height(), true);
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
}
