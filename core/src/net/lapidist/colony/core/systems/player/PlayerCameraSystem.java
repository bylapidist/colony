package net.lapidist.colony.core.systems.player;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import net.lapidist.colony.core.systems.abstracts.AbstractCameraSystem;

@Wire
public class PlayerCameraSystem extends AbstractCameraSystem {

    public PlayerCameraSystem(final float zoom) {
        super(Aspect.all(), zoom);
    }

    @Override
    protected final void process(final int e) {
        getCamera().update();
    }
}
