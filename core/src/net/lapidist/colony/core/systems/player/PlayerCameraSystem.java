package net.lapidist.colony.core.systems.player;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import net.lapidist.colony.components.player.PlayerComponent;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.systems.abstracts.AbstractCameraSystem;

import static com.artemis.E.E;

@Wire
public class PlayerCameraSystem extends AbstractCameraSystem {

    public PlayerCameraSystem(float zoom) {
        super(Aspect.all(PlayerComponent.class), zoom);
    }

    @Override
    protected void process(int e) {
        camera.position.x = E(e).dynamicBodyComponentBody().getPosition().scl(Constants.PPM).x;
        camera.position.y = E(e).dynamicBodyComponentBody().getPosition().scl(Constants.PPM).y;

        camera.update();
    }
}
