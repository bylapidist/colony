package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 11 to 12 with no data changes. */
public final class V11ToV12Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V11.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V12.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
