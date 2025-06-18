package net.lapidist.colony.save;

import net.lapidist.colony.components.state.map.MapState;

/**
 * Represents a single save migration step between two versions.
 */
public interface MapStateMigration {
    /**
     * @return the version this migration upgrades from
     */
    int fromVersion();

    /**
     * @return the version this migration upgrades to
     */
    int toVersion();

    /**
     * Applies the migration to the supplied state and returns the upgraded result.
     *
     * @param state map state to migrate
     * @return migrated state
     */
    MapState apply(MapState state);
}
