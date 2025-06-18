package net.lapidist.colony.save;

import net.lapidist.colony.components.state.map.MapState;

/** Migration from save version 35 to 36 adding inventory storage. */
public final class V35ToV36Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V35.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V36.number();
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .inventory(new java.util.HashMap<>())
                .version(toVersion())
                .build();
    }
}
