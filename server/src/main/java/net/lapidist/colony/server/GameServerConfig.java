package net.lapidist.colony.server;

import net.lapidist.colony.config.ColonyConfig;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.map.ChunkedMapGenerator;
import net.lapidist.colony.map.MapGenerator;

/**
 * Configuration object for {@link GameServer} instances.
 */
public final class GameServerConfig {

    static final String DEFAULT_SAVE_NAME = ColonyConfig.get().getString("game.defaultSaveName");
    static final long DEFAULT_INTERVAL = ColonyConfig.get().getLong("game.autosaveInterval");
    static final int DEFAULT_WIDTH = GameConstants.MAP_WIDTH;
    static final int DEFAULT_HEIGHT = GameConstants.MAP_HEIGHT;

    private String saveName = DEFAULT_SAVE_NAME;
    private long autosaveInterval = DEFAULT_INTERVAL;
    private MapGenerator mapGenerator = new ChunkedMapGenerator();
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;

    public String getSaveName() {
        return saveName;
    }

    public long getAutosaveInterval() {
        return autosaveInterval;
    }

    public MapGenerator getMapGenerator() {
        return mapGenerator;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

        public Builder autosaveInterval(final long interval) {
            config.autosaveInterval = interval;
            return this;
        }

        public Builder mapGenerator(final MapGenerator generator) {
            config.mapGenerator = generator;
            return this;
        }

        public Builder width(final int w) {
            config.width = w;
            return this;
        }

        public Builder height(final int h) {
            config.height = h;
            return this;
        }

        public GameServerConfig build() {
            return config;
        }
    }
}
