package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

import java.util.Map;

/**
 * Portion of the map sent from the server to clients.
 */
@KryoType
public record MapChunk(int chunkX, int chunkY, Map<TilePos, TileData> tiles) { }
