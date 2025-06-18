package net.lapidist.colony.client.systems.network;

import net.lapidist.colony.client.systems.MapInitSystem;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.map.ProvidedMapStateProvider;

public final class MapLoadSystem extends MapInitSystem {

    public MapLoadSystem(final MapState state) {
        super(new ProvidedMapStateProvider(state));
    }
}
