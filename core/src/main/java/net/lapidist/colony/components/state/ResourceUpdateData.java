package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Broadcast message containing updated resources for a tile.
 *
 * @param x     tile x coordinate
 * @param y     tile y coordinate
 * @param wood  wood amount
 * @param stone stone amount
 * @param food  food amount
 */
@KryoType
public record ResourceUpdateData(int x, int y, int wood, int stone, int food) { }
