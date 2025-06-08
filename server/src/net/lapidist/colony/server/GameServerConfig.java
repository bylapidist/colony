package net.lapidist.colony.server;

import net.lapidist.colony.config.ColonyConfig;
import net.lapidist.colony.map.DefaultMapGenerator;
import net.lapidist.colony.map.MapGenerator;

/**
 * Configuration object for {@link GameServer} instances.
 */
public final class GameServerConfig {

    static final String DEFAULT_SAVE_NAME = ColonyConfig.get().getString("game.defaultSaveName");
    static final long DEFAULT_INTERVAL_MS = ColonyConfig.get().getLong("game.autosaveIntervalMs");

    private String saveName = DEFAULT_SAVE_NAME;
    private long autosaveIntervalMs = DEFAULT_INTERVAL_MS;
    private MapGenerator mapGenerator = new DefaultMapGenerator();

    public String getSaveName() {
        return saveName;
    }

    public long getAutosaveIntervalMs() {
        return autosaveIntervalMs;
    }

    public MapGenerator getMapGenerator() {
        return mapGenerator;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final GameServerConfig config = new GameServerConfig();

        public Builder saveName(final String saveName) {
            config.saveName = saveName;
            return this;
        }

        public Builder autosaveIntervalMs(final long interval) {
            config.autosaveIntervalMs = interval;
            return this;
        }

        public Builder mapGenerator(final MapGenerator generator) {
            config.mapGenerator = generator;
            return this;
        }

        public GameServerConfig build() {
            return config;
        }
    }
}
