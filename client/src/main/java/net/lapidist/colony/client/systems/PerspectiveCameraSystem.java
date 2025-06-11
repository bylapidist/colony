package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Rectangle;

/** Simple perspective camera implementation. */
public final class PerspectiveCameraSystem extends BaseSystem implements CameraProvider {

    private static final float DEFAULT_FOV = 67f;
    private static final float DEFAULT_Z = 10f;
    private static final float NEAR = 1f;
    private static final float FAR = 100f;

    private final PerspectiveCamera camera;
    private final ScreenViewport viewport;

    public PerspectiveCameraSystem() {
        camera = new PerspectiveCamera(DEFAULT_FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(new Vector3(0, 0, DEFAULT_Z));
        camera.lookAt(0, 0, 0);
        camera.near = NEAR;
        camera.far = FAR;
        viewport = new ScreenViewport(camera);
        viewport.apply();
    }

    @Override
    protected void processSystem() {
        camera.update();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public Viewport getViewport() {
        return viewport;
    }

    @Override
    public Rectangle getVisibleTileBounds() {
        return new Rectangle();
    }
}
