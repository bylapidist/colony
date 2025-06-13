package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Player position in tile coordinates.
 *
 * @param x tile x coordinate
 * @param y tile y coordinate
 */
@KryoType
public record PlayerPosition(int x, int y) { }
