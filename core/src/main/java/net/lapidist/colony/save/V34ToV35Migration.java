package net.lapidist.colony.save;

import net.lapidist.colony.components.state.EnvironmentState;
import net.lapidist.colony.components.state.map.MapState;

/** Migration from save version 34 to 35 adding environment state. */
public final class V34ToV35Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V34.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V35.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .environment(new EnvironmentState())
                .version(toVersion())
                .build();
    }
}
