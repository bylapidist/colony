package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Coordinate key used for tile lookup in {@link MapState}.
 */
@KryoType
public record TilePos(int x, int y) { }
