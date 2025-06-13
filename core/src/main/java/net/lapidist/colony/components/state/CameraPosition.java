package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Camera position in world coordinates.
 *
 * @param x camera X coordinate
 * @param y camera Y coordinate
 */
@KryoType
public record CameraPosition(float x, float y) {
}
