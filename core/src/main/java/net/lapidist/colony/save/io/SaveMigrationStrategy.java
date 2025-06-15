package net.lapidist.colony.save.io;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.save.SaveData;

/**
 * Applies migrations when loading save files.
 */
public interface SaveMigrationStrategy {
    MapState migrate(SaveData data);
}
