package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.utils.Disposable;
import box2dLight.RayHandler;

/**
 * Updates an optional {@link RayHandler} each frame and disposes it when no longer needed.
 */
public final class LightingSystem extends BaseSystem implements Disposable {

    private RayHandler rayHandler;

    /** Assign the handler used for lighting. */
    public void setRayHandler(final RayHandler handler) {
        this.rayHandler = handler;
    }

    /** Current lighting handler or {@code null}. */
    public RayHandler getRayHandler() {
        return rayHandler;
    }

    @Override
    protected void processSystem() {
        if (rayHandler != null) {
            rayHandler.update();
        }
    }

    @Override
    public void dispose() {
        if (rayHandler != null) {
            rayHandler.dispose();
        }
    }
}
