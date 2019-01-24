package net.lapidist.colony.core.systems.camera;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.lapidist.colony.components.player.PlayerComponent;

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
                E(e).positionComponentPosition().x / cameraSystem.zoom;

        cameraSystem.camera.position.y =
                E(e).positionComponentPosition().y / cameraSystem.zoom;

        cameraSystem.camera.update();
    }
}
