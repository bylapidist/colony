package net.lapidist.colony.core.systems.camera;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.lapidist.colony.components.PlayerComponent;
import net.lapidist.colony.core.Constants;

import static com.artemis.E.E;

@Wire
public class PlayerCameraSystem extends EntityProcessingSystem {

    private CameraSystem cameraSystem;

    public PlayerCameraSystem() {
        super(Aspect.all(PlayerComponent.class));
    }

    @Override
    protected void process(Entity e) {
        cameraSystem.camera.position.x =
                E(e).getSpriteComponent().getSprite().getX()
                + Constants.PPM / cameraSystem.zoom;
        cameraSystem.camera.position.y = E(e).getSpriteComponent().getSprite().getY()
                + Constants.PPM / cameraSystem.zoom;
    }
}
