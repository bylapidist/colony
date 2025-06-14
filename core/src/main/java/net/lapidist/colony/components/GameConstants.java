package net.lapidist.colony.components;

import net.lapidist.colony.config.ColonyConfig;

public final class GameConstants {
    private GameConstants() { }

    public static final int TILE_SIZE = ColonyConfig.get().getInt("game.tileSize");
    public static final int CHUNK_LOAD_RADIUS = ColonyConfig.get().getInt("game.chunkLoadRadius");
    public static final int NETWORK_BUFFER_SIZE = ColonyConfig.get().getInt("game.networkBufferSize");
}
