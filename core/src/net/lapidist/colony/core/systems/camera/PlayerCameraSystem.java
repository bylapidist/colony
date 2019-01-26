package net.lapidist.colony.core.systems.camera;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import net.lapidist.colony.components.player.PlayerComponent;

import static com.artemis.E.E;

@Wire
public class PlayerCameraSystem extends AbstractCameraSystem {

    public PlayerCameraSystem(float zoom) {
        super(Aspect.all(PlayerComponent.class), zoom);
    }

    @Override
    protected void process(int e) {
        camera.position.x =
                E(e).positionComponentPosition().x / zoom;

        camera.position.y =
                E(e).positionComponentPosition().y / zoom;

        camera.update();
    }
}
