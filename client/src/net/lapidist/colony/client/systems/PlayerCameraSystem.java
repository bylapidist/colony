package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.lapidist.colony.client.util.CameraUtils;

public final class PlayerCameraSystem extends BaseSystem {


    private OrthographicCamera camera;

    private ExtendViewport viewport;

    public PlayerCameraSystem() {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        // Set camera to center of world
        moveCameraToWorldCoords(CameraUtils.getWorldCenter());

        viewport.apply();
    }

    public void moveCameraToWorldCoords(final Vector2 worldCoords) {
        viewport.getCamera().translate(worldCoords.x, worldCoords.y, 0);
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
