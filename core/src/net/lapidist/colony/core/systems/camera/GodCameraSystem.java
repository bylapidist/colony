package net.lapidist.colony.core.systems.camera;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import net.lapidist.colony.core.systems.AbstractCameraSystem;

@Wire
public class GodCameraSystem extends AbstractCameraSystem {

    public GodCameraSystem(float zoom) {
        super(Aspect.all(), zoom);
    }

    @Override
    protected void process(int e) {
        camera.update();
    }
}
