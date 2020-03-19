package net.lapidist.colony.core.systems.abstracts;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public abstract class AbstractCameraSystem extends IteratingSystem {

    private static Vector3 tmpVec3 = new Vector3();
    private static Vector2 tmpVec2 = new Vector2();
    private final float zoom;
    private OrthographicCamera camera;
    private OrthographicCamera guiCamera;

    public AbstractCameraSystem(
            final Aspect.Builder aspect,
            final float zoomToSet
    ) {
        super(aspect);

        this.zoom = 1;
        float zoomFactorInverter = 1f / zoomToSet;

        setupViewport(
                Gdx.graphics.getWidth() * zoomFactorInverter,
                Gdx.graphics.getHeight() * zoomFactorInverter
        );
    }

    private void setupViewport(final float width, final float height) {
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);
        camera.update();

        guiCamera = new OrthographicCamera(width, height);
        guiCamera.setToOrtho(false, width, height);
        guiCamera.update();
    }

    public final Vector2 screenCoordsFromWorldCoords(
            final float worldX,
            final float worldY
    ) {
        camera.project(tmpVec3.set(worldX, worldY, 0));
        return tmpVec2.set(tmpVec3.x, tmpVec3.y);
    }

    public final Vector2 worldCoordsFromScreenCoords(
            final float screenX,
            final float screenY
    ) {
        camera.unproject(tmpVec3.set(screenX, screenY, 0));
        return tmpVec2.set(tmpVec3.x, tmpVec3.y);
    }

    @Override
    protected void process(final int entityId) {
    }

    public final float getZoom() {
        return zoom;
    }

    public final OrthographicCamera getCamera() {
        return camera;
    }

    public final void setCamera(final OrthographicCamera cameraToSet) {
        this.camera = cameraToSet;
    }

    public final OrthographicCamera getGuiCamera() {
        return guiCamera;
    }

    public final void setGuiCamera(final OrthographicCamera guiCameraToSet) {
        this.guiCamera = guiCameraToSet;
    }
}
