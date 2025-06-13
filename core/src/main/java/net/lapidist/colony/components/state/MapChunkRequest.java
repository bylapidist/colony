package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Request message sent by clients to retrieve a map chunk.
 *
 * @param chunkX chunk x coordinate
 * @param chunkY chunk y coordinate
 */
@KryoType
public record MapChunkRequest(int chunkX, int chunkY) { }
