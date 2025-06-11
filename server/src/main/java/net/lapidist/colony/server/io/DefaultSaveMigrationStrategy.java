package net.lapidist.colony.server.io;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.save.SaveData;
import net.lapidist.colony.save.SaveMigrator;
import net.lapidist.colony.save.SaveVersion;

/**
 * Default migration strategy using {@link SaveMigrator}.
 */
public final class DefaultSaveMigrationStrategy implements SaveMigrationStrategy {
    @Override
    public MapState migrate(final SaveData data) {
        MapState state = data.mapState();
        if (data.version() < SaveVersion.CURRENT.number()) {
            state = SaveMigrator.migrate(state, data.version(), SaveVersion.CURRENT.number());
        }
        return state;
    }
}
