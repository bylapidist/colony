package net.lapidist.colony.core.systems.mobs;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.CollectorComponent;
import net.lapidist.colony.core.states.CollectorState;

import static com.artemis.E.*;

@Wire
public class CollectorControlSystem extends IteratingSystem {

    public CollectorControlSystem() {
        super(Aspect.all(CollectorComponent.class));
    }

    @Override
    protected void process(int e) {
        if (E(e).unitComponentState() == CollectorState.IDLE) {
            handleIdle(e);
        }

        if (E(e).unitComponentState() == CollectorState.WANDERING) {
            handleWander(e);
        }
    }

    private void handleIdle(int e) {
        if ((int)(Math.random() * (10 + 1)) > 9) {
            E(e).unitComponentState(CollectorState.WANDERING);
        }
    }

    private void handleWander(int e) {
        E(e).velocityComponentVelocity(E(e).velocityComponentVelocity().add(
                new Vector2(
                        -1 + (int)(Math.random() * ((1 - -1) + 1)),
                        -1 + (int)(Math.random() * ((1 - -1) + 1))
                )
        ));

        if ((int)(Math.random() * (10 + 1)) > 2) {
            E(e).unitComponentState(CollectorState.IDLE);
        }
    }
}
