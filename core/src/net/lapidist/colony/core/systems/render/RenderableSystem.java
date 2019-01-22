package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import net.lapidist.colony.components.render.RenderableComponent;
import net.lapidist.colony.core.systems.camera.CameraSystem;

import static com.artemis.E.E;

public class RenderableSystem extends IteratingSystem {

    private CameraSystem cameraSystem;

    public RenderableSystem() {
        super(Aspect.all(RenderableComponent.class));
    }

    @Override
    protected void process(final int e) {
//        if (cameraSystem.outOfBounds(
//                E(e).spriteComponentSprite().getX(),
//                E(e).spriteComponentSprite().getY()
//        )) {
//            E(e).renderableComponentRenderable(false);
//            return;
//        }

        E(e).renderableComponentRenderable(cameraSystem.outOfBounds(
                100,
                100
        ));
    }
}
