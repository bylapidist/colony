package net.lapidist.colony.save;

import net.lapidist.colony.components.state.MapState;

/** Migration from save version 5 to version 6 removing texture references. */
public final class V5ToV6Migration implements MapStateMigration {
    @Override
    public int fromVersion() {
        return SaveVersion.V5.number();
    }

    @Override
    public int toVersion() {
        return SaveVersion.V6.number();
    }

    @Override
    public MapState apply(final MapState state) {
        // Texture references are ignored in this version; just bump the version.
        return state.toBuilder()
                .version(toVersion())
                .build();
    }
}
