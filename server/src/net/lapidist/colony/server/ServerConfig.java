package net.lapidist.colony.server;

import net.lapidist.colony.map.MapGenerator;

/**
 * Generic configuration options used by the {@link GameServer}.
 */
public interface ServerConfig {
    /**
     * @return the name of the save file to load or create
     */
    String getSaveName();

    /**
     * @return the interval in milliseconds between autosaves
     */
    long getAutosaveInterval();

    /**
     * @return the map generator to use when creating a new world
     */
    MapGenerator getMapGenerator();
}
