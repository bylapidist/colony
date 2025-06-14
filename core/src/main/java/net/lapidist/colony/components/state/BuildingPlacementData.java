package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * Request message sent by clients when placing a building.
 *
 * @param x            tile x coordinate
 * @param y            tile y coordinate
 * @param buildingId building identifier
 */
@KryoType
public record BuildingPlacementData(int x, int y, String buildingId) {
}
