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

    private static Vector2 tmpVec2 = new Vector2();
    private static Vector3 tmpVec3 = new Vector3();
    private static Vector2 mouse = new Vector2();
    final float zoom;

    public CameraSystem() {
        this(1);
    }

    public CameraSystem(final float width, final float height) {
        this.zoom = 1;
        setupViewport(width, height);
    }

    public CameraSystem(final float zoom) {
        this.zoom = zoom;
        float zoomFactorInverter = 1f / zoom;

        setupViewport(Gdx.graphics.getWidth() * zoomFactorInverter, Gdx.graphics.getHeight() * zoomFactorInverter);
    }

    protected void setupViewport(final float width, final float height) {
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);
        camera.zoom = zoom;
        camera.update();

        guiCamera = new OrthographicCamera(width, height);
        guiCamera.setToOrtho(false, width, height);
        guiCamera.update();
    }

    public boolean outOfBounds(final float screenX, final float screenY) {
        return screenX < -Constants.PPM * 2
                || screenX > Gdx.graphics.getWidth() + Constants.PPM
                || screenY < -Constants.PPM * 2
                || screenY > Gdx.graphics.getHeight() + Constants.PPM;
    }

    public Vector2 screenCoords(final float worldX, final float worldY) {
        camera.update();
        camera.project(tmpVec3.set(worldX, worldY, 0));

        return mouse.set(tmpVec3.x, tmpVec3.y);
    }

    public Vector2 worldCoords(final float screenX, final float screenY) {
        camera.update();
        camera.unproject(tmpVec3.set(screenX, screenY, 0));

        return mouse.set(tmpVec3.x, tmpVec3.y);
    }

    @Override
    protected void processSystem() {

    }
}
