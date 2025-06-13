package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Client request for a specific map chunk.
 */
@KryoType
public record ChunkRequest(int chunkX, int chunkY) { }
