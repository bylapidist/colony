package net.lapidist.colony.core.systems.mobs;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import net.lapidist.colony.components.CollectorComponent;

@Wire
public class CollectorControlSystem extends IteratingSystem {

    public CollectorControlSystem() {
        super(Aspect.all(CollectorComponent.class));
    }

    @Override
    protected void process(int entityId) {

    }
}
