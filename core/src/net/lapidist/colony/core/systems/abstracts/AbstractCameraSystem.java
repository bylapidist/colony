package net.lapidist.colony.core.systems.abstracts;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public abstract class AbstractCameraSystem extends IteratingSystem {

    public OrthographicCamera camera;
    public OrthographicCamera guiCamera;
    protected static Vector3 tmpVec3 = new Vector3();
    protected static Vector2 tmpVec2 = new Vector2();

    public final float zoom;

    public AbstractCameraSystem(Aspect.Builder aspect, float zoom) {
        super(aspect);

        this.zoom = 1;
        float zoomFactorInverter = 1f / zoom;

        setupViewport(Gdx.graphics.getWidth() * zoomFactorInverter, Gdx.graphics.getHeight() * zoomFactorInverter);
    }

    private void setupViewport(float width, float height) {
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);
        camera.update();

        guiCamera = new OrthographicCamera(width, height);
        guiCamera.setToOrtho(false, width, height);
        guiCamera.update();
    }

    @Override
    protected void process(int entityId) {

    }
}
