package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Request message sent when a building should be removed.
 *
 * @param x tile x coordinate
 * @param y tile y coordinate
 */
@KryoType
public record BuildingRemovalData(int x, int y) {
}
