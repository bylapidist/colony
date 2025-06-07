package net.lapidist.colony.client.systems.network;

import com.artemis.BaseSystem;
import net.lapidist.colony.client.entities.factories.MapFactory;
import net.lapidist.colony.components.state.MapState;

public final class MapLoadSystem extends BaseSystem {
    private final MapState state;

    public MapLoadSystem(final MapState stateToSet) {
        this.state = stateToSet;
    }

    @Override
    public void initialize() {
        MapFactory.create(world, state);
    }

    @Override
    protected void processSystem() {
        // loading occurs once in initialize
    }
}
