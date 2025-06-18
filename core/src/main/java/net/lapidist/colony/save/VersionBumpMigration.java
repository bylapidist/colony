package net.lapidist.colony.save;

import net.lapidist.colony.components.state.map.MapState;

/**
 * Simple migration that only updates the save version without altering the state.
 */
public final class VersionBumpMigration implements MapStateMigration {
    private final int fromVersion;
    private final int toVersion;

    public VersionBumpMigration(final SaveVersion from, final SaveVersion to) {
        this.fromVersion = from.number();
        this.toVersion = to.number();
    }

    @Override
    public int fromVersion() {
        return fromVersion;
    }

    @Override
    public int toVersion() {
        return toVersion;
    }

    @Override
    public MapState apply(final MapState state) {
        return state.toBuilder()
                .version(toVersion)
                .build();
    }
}
