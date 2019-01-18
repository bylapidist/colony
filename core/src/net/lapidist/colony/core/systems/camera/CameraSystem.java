package net.lapidist.colony.core.systems.camera;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.core.Constants;

public class CameraSystem extends BaseSystem {

    public OrthographicCamera camera;
    public OrthographicCamera guiCamera;

    private static Vector3 tmpVec3 = new Vector3();
    private static Vector2 mouse = new Vector2();
    final float zoom;

    public CameraSystem() {
        this(1);
    }

    public CameraSystem(float width, float height) {
        this.zoom = 1;
        setupViewport(width, height);
    }

    public CameraSystem(float zoom) {
        this.zoom = zoom;
        float zoomFactorInverter = 1f / zoom;

        setupViewport(Gdx.graphics.getWidth() * zoomFactorInverter, Gdx.graphics.getHeight() * zoomFactorInverter);
    }

    protected void setupViewport(float width, float height) {
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);
        camera.zoom = zoom;
        camera.update();

        guiCamera = new OrthographicCamera(width, height);
        guiCamera.setToOrtho(false, width, height);
        guiCamera.update();
    }

    public boolean outOfBounds(float worldX, float worldY) {
        Vector2 screenCoords = screenCoords(worldX, worldY);

        return screenCoords.x < -Constants.PPM * 2
                || screenCoords.x > Gdx.graphics.getWidth() + Constants.PPM
                || screenCoords.y < -Constants.PPM * 2
                || screenCoords.y > Gdx.graphics.getHeight() + Constants.PPM;
    }

    public Vector2 screenCoords(float worldX, float worldY) {
        camera.project(tmpVec3.set(worldX, worldY, 0));

        return mouse.set(tmpVec3.x, tmpVec3.y);
    }

    public Vector2 worldCoords(float screenX, float screenY) {
        camera.unproject(tmpVec3.set(screenX, screenY, 0));

        return mouse.set(tmpVec3.x, tmpVec3.y);
    }

    @Override
    protected void processSystem() {

    }
}
