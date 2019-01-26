package net.lapidist.colony.core.systems.player;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import net.lapidist.colony.components.player.PlayerComponent;
import net.lapidist.colony.core.systems.abstracts.AbstractCameraSystem;
import net.lapidist.colony.core.systems.map.MapGenerationSystem;

import static com.artemis.E.E;

@Wire
public class PlayerCameraSystem extends AbstractCameraSystem {

    private MapGenerationSystem mapGenerationSystem;

    public PlayerCameraSystem(float zoom) {
        super(Aspect.all(PlayerComponent.class), zoom);
    }

    @Override
    protected void process(int e) {
        camera.position.x = E(e).dynamicBodyComponentBody().getPosition().scl(mapGenerationSystem.getTileWidth()).x;
        camera.position.y = E(e).dynamicBodyComponentBody().getPosition().scl(mapGenerationSystem.getTileHeight()).y;

        camera.update();
    }
}
