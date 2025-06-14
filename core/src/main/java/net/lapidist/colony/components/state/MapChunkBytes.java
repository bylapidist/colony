package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Compressed map chunk payload sent from the server to clients.
 */
@KryoType
public record MapChunkBytes(int chunkX, int chunkY, byte[] data) { }
