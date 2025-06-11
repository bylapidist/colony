package net.lapidist.colony.map;

import net.lapidist.colony.components.state.MapState;

/**
 * Returns a pre-existing {@link MapState}.
 */
public final class ProvidedMapStateProvider implements MapStateProvider {
    private final MapState state;

    public ProvidedMapStateProvider(final MapState stateToSet) {
        this.state = stateToSet;
    }

    @Override
    public MapState provide() {
        return state;
    }
}
