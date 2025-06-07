package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.lapidist.colony.client.core.Constants;

public final class PlayerCameraSystem extends BaseSystem {

    private final Vector3 tmpVec3 = new Vector3();

    private final Vector2 tmpVec2 = new Vector2();

    private OrthographicCamera camera;

    private ExtendViewport viewport;

    public PlayerCameraSystem() {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        // Set camera to center of world
        moveCameraToWorldCoords(getWorldCenter());

        viewport.apply();
    }

    public boolean withinCameraView(final Vector2 screenCoords) {
        Vector2 cameraCoords = cameraCoordsFromWorldCoords(screenCoords.x, screenCoords.y);

        return !(cameraCoords.x > Gdx.graphics.getWidth()
                || cameraCoords.y > Gdx.graphics.getHeight()
        );
    }

    public void moveCameraToWorldCoords(final Vector2 worldCoords) {
        viewport.getCamera().translate(worldCoords.x, worldCoords.y, 0);
    }

    public Vector2 getWorldCenter() {
        return new Vector2(
                (Constants.TILE_SIZE * Constants.MAP_WIDTH + Constants.TILE_SIZE) / 2f,
                (Constants.TILE_SIZE * Constants.MAP_HEIGHT + Constants.TILE_SIZE) / 2f
        );
    }

    public Vector2 cameraCoordsFromWorldCoords(
            final float worldX,
            final float worldY
    ) {
        camera.project(tmpVec3.set(worldX, worldY, 0));
        return tmpVec2.set(tmpVec3.x, tmpVec3.y);
    }

    public Vector2 worldCoordsFromCameraCoords(
            final float screenX,
            final float screenY
    ) {
        camera.unproject(tmpVec3.set(screenX, screenY, 0));
        return tmpVec2.set(tmpVec3.x, tmpVec3.y);
    }

    public Vector2 tileCoordsToWorldCoords(final int x, final int y) {
        return new Vector2(
                x * Constants.TILE_SIZE,
                y * Constants.TILE_SIZE
        );
    }

    public Vector2 tileCoordsToWorldCoords(final Vector2 coords) {
        return tileCoordsToWorldCoords((int) coords.x, (int) coords.y);
    }

    public Vector2 worldCoordsToTileCoords(final int x, final int y) {
        return new Vector2(
                Math.floorDiv(x, Constants.TILE_SIZE),
                Math.floorDiv(y, Constants.TILE_SIZE)
        );
    }

    public Vector2 worldCoordsToTileCoords(final Vector2 coords) {
        return worldCoordsToTileCoords((int) coords.x, (int) coords.y);
    }

    @Override
    protected void processSystem() {
        camera.update();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public float getZoom() {
        return camera.zoom;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(final OrthographicCamera cameraToSet) {
        this.camera = cameraToSet;
    }

    public ExtendViewport getViewport() {
        return viewport;
    }

    public void setViewport(final ExtendViewport viewportToSet) {
        this.viewport = viewportToSet;
    }
}
