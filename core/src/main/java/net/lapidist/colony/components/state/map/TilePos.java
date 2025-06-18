package net.lapidist.colony.components.state.map;

import net.lapidist.colony.serialization.KryoType;

/**
 * Coordinate key used for tile lookup in {@link MapState}.
 *
 * @param x tile x coordinate
 * @param y tile y coordinate
 */
@KryoType
public record TilePos(int x, int y) { }
