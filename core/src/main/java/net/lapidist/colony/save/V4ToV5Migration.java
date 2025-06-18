package net.lapidist.colony.save;

import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.resources.ResourceData;

/** Migration from save version 4 to version 5 adding player resources. */
public final class V4ToV5Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V4.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V5.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .playerResources(new ResourceData())
                .version(toVersion())
                .build();
    }
}
