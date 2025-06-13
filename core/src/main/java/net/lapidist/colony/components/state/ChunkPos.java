package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Coordinate key used for chunk lookup.
 *
 * @param x chunk x coordinate
 * @param y chunk y coordinate
 */
@KryoType
public record ChunkPos(int x, int y) { }
