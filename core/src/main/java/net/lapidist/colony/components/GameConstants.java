package net.lapidist.colony.components;

import net.lapidist.colony.config.ColonyConfig;

public final class GameConstants {
    private GameConstants() { }

    public static final int MAP_WIDTH = ColonyConfig.get().getInt("game.mapWidth");
    public static final int MAP_HEIGHT = ColonyConfig.get().getInt("game.mapHeight");
    public static final int TILE_SIZE = ColonyConfig.get().getInt("game.tileSize");
    public static final int MAP_CHUNK_SIZE = ColonyConfig.get().getInt("game.mapChunkSize");
}
