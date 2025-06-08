package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.client.entities.factories.MapFactory;
import net.lapidist.colony.map.MapStateProvider;

/**
 * Initializes the game world using a {@link MapStateProvider}.
 */
public class MapInitSystem extends BaseSystem {
    private final MapStateProvider provider;

    public MapInitSystem(final MapStateProvider providerToSet) {
        this.provider = providerToSet;
    }

    @Override
    public final void initialize() {
        MapFactory.create(world, provider.provide());
    }

    @Override
    protected final void processSystem() {
        // initialization occurs once in initialize
    }
}
